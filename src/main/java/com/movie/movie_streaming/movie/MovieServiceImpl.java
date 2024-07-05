package com.movie.movie_streaming.movie;

import com.movie.movie_streaming.Utilities.QuadFunction;
import com.movie.movie_streaming.actor.Actor;
import com.movie.movie_streaming.actor.ActorRepository;
import com.movie.movie_streaming.comment.Comment;
import com.movie.movie_streaming.comment.CommentMapper;
import com.movie.movie_streaming.comment.CommentRepository;
import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.PostCommentRequest;
import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.director.Director;
import com.movie.movie_streaming.director.DirectorRepository;
import com.movie.movie_streaming.exceptions.ActorNotFoundException;
import com.movie.movie_streaming.exceptions.DirectorNotFoundException;
import com.movie.movie_streaming.exceptions.MovieAlreadyExistsException;
import com.movie.movie_streaming.exceptions.MovieNotFoundException;
import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.movie.dto.UpdateMovieRequest;
import com.movie.movie_streaming.person.Person;
import com.movie.movie_streaming.person.PersonMapper;
import com.movie.movie_streaming.person.dto.PersonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.movie.movie_streaming.Utilities.Utils.generatePageResponse;
import static com.movie.movie_streaming.Utilities.Utils.updateFieldIfNotNull;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final MovieMapper movieMapper;
    private final PersonMapper personMapper;

    @Override
    public MovieDisplay saveMovie(SaveMovieRequest request) {
        Movie movie = movieMapper.toMovie(request);
        movie.setActors(personMapper.toActorSet(request.actors()));
        movie.setDirectors(personMapper.toDirectorSet(request.directors()));

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("yearReleased", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("duration", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("rating", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("genres", ExampleMatcher.GenericPropertyMatchers.exact());

        Example<Movie> movieExample = Example.of(movie, matcher);

        if (movieRepository.exists(movieExample)) {
            throw new MovieAlreadyExistsException();
        }
        movie = movieRepository.save(movie);
        Set<Actor> actors = checkAndSavePeople(
                request.actors(),
                personMapper::toActor,
                actorRepository::findUniqueActor,
                actorRepository::save,
                Actor::addMovieActed,
                movie
        );
        movie.setActors(actors);
        Set<Director> directors = checkAndSavePeople(
                request.directors(),
                personMapper::toDirector,
                directorRepository::findUniqueDirector,
                directorRepository::save,
                Director::addMovieDirected,
                movie
        );
        movie.setDirectors(directors);

        movie = movieRepository.save(movie);

        return movieMapper.toDisplay(movie);
    }
    @Override
    public MovieDisplay findMovieById(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        MovieDisplay display = movieMapper.toDisplay(movie);

        Page<Comment> page = commentRepository.findAllWithMovieId(
                id,
                PageRequest.of(0,5,
                        Sort.by("createdDate").descending())
        );

        display.setComments(
                page.stream()
                .map(commentMapper::toDisplay)
                .collect(Collectors.toList())
        );
        return display;
    }
    @Override
    public PageResponse<MovieResponse> findAllMovies(int page, int size) {
        Pageable pageable = generatePageableSortedByDate(page, size);
        Page<Movie> movies = movieRepository.findAll(pageable);
        return generatePageResponse(movies,movieMapper::toResponse);
    }

    @Override
    public PageResponse<MovieResponse> findAllMoviesWithSearchTerm(int page, int size, String searchTerm) {
        Pageable pageable = generatePageableSortedByDate(page, size);
        Page<Movie> movies = movieRepository.findAllMoviesByTerm(searchTerm.trim(),pageable);
        return generatePageResponse(movies,movieMapper::toResponse);
    }


    @Override
    public PageResponse<MovieResponse> findAllMoviesWithGenres(int page, int size, Set<Genre> genres) {
        Pageable pageable = generatePageableSortedByDate(page, size);
        Page<Movie> movies = movieRepository.findAllMoviesWithGenres(genres,pageable);
        return generatePageResponse(movies,movieMapper::toResponse);
    }
    @Override
    public PageResponse<MovieResponse> findAllMoviesWithActor(int page, int size, Integer id) {
        Pageable pageable = PageRequest.of(page,size);

        if (!actorRepository.existsById(id)){
            throw new ActorNotFoundException(id);
        }

        Page<Movie> movies = actorRepository.findMoviesByActor(id, pageable);

        return generatePageResponse(movies,movieMapper::toResponse);
    }
    @Override
    public PageResponse<MovieResponse> findAllMoviesByDirector(int page, int size, Integer id) {
        Pageable pageable = PageRequest.of(page,size);

        if (!directorRepository.existsById(id)){
            throw new DirectorNotFoundException(id);
        }

        Page<Movie> movies = directorRepository.findMoviesByDirector(id,pageable);

        return generatePageResponse(movies,movieMapper::toResponse);
    }
    @Override
    public CommentDisplay addCommentToMovie(PostCommentRequest request, Integer movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(movieId));

        Comment comment = commentMapper.toComment(request);
        comment.setMovie(movie);

        comment = commentRepository.save(comment);

        return commentMapper.toDisplay(comment);
    }

    @Override
    public MovieDisplay updateMovie(Integer id, UpdateMovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        if (request == null){
            throw new IllegalArgumentException("Update request must not be null");
        }

        updateFieldIfNotNull(request.title(), movie::setTitle);
        updateFieldIfNotNull(request.synopsis(), movie::setSynopsis);
        updateFieldIfNotNull(request.rating(), movie::setRating);
        updateFieldIfNotNull(request.trailerLink(), movie::setTrailerLink);
        updateFieldIfNotNull(request.movieLink(),movie::setMovieLink);
        updateFieldIfNotNull(request.poster(), movie::setPoster);
        updateFieldIfNotNull(request.yearReleased(), movie::setYearReleased);
        updateFieldIfNotNull(request.duration(), movie::setDuration);
        updateFieldIfNotNull(request.genres(), movie::setGenres);

        updatePeople(
                request.directors(),
                movie.getDirectors(),
                personMapper::toDirectorSet,
                directorRepository::findUniqueDirector,
                directorRepository::save,
                Director::addMovieDirected,
                Director::getMoviesInternal,
                directorRepository::deleteById,
                movie
        );

        updatePeople(
                request.actors(),
                movie.getActors(),
                personMapper::toActorSet,
                actorRepository::findUniqueActor,
                actorRepository::save,
                Actor::addMovieActed,
                Actor::getMoviesInternal,
                actorRepository::deleteById,
                movie
        );

        movieRepository.save(movie);

        return movieMapper.toDisplay(movie);
    }

    @Override
    public String deleteById(Integer movieId) {
        if(!movieRepository.existsById(movieId)){
            throw new MovieNotFoundException(movieId);
        }
        movieRepository.deleteById(movieId);
        return "Movie deleted succesfully.";
    }

    private Pageable generatePageableSortedByDate(int page, int size){
        return PageRequest.of(
                page,
                size,
                Sort.by("yearReleased").descending()
        );
    }
    private <T extends Person> Set<T> checkAndSavePeople(
            Set<PersonRequest> personRequests,
            Function<PersonRequest, T> toPerson,
            QuadFunction<String, String, String, LocalDate, Optional<T>> findUniquePerson,
            Function<T, T> savePerson,
            BiConsumer<T, Movie> addMovieToPerson,
            Movie movie
    ) {
        return personRequests.stream()
                .map(toPerson)
                .map(person -> findUniquePerson.apply(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getCountry(),
                        person.getBirthDate()
                ).orElseGet(() -> savePerson.apply(person)))
                .peek(person -> addMovieToPerson.accept(person, movie))
                .collect(Collectors.toSet());
    }

    private <T extends Person> void updatePeople(
            Set<PersonRequest> personRequests,
            Set<T> currentPeople,
            Function<Set<PersonRequest>, Set<T>> toPersonSet,
            QuadFunction<String, String, String, LocalDate, Optional<T>> findUniquePerson,
            Function<T, T> savePerson,
            BiConsumer<T, Movie> addMovieToPerson,
            Function<T,Set<Movie>> getMovies,
            Consumer<Integer> deleteById,
            Movie movie
    ) {
        if (personRequests == null) {
            return;
        }

        Set<T> newPeople = toPersonSet.apply(personRequests);
        Iterator<T> iterator = currentPeople.iterator();

        while (iterator.hasNext()) {
            T person = iterator.next();
            if (!newPeople.contains(person)) {
                iterator.remove();
                getMovies.apply(person).remove(movie);
                if (getMovies.apply(person).isEmpty()) {
                    deleteById.accept(person.getId());
                }
            }
        }
        for (T newPerson : newPeople) {
            if (currentPeople.contains(newPerson)) continue;
            Optional<T> opt = findUniquePerson.apply(
                    newPerson.getFirstName(),
                    newPerson.getLastName(),
                    newPerson.getCountry(),
                    newPerson.getBirthDate()
            );
            T person = opt.orElse(newPerson);
            addMovieToPerson.accept(person, movie);
            currentPeople.add(person);
            savePerson.apply(person);
        }
    }


}
