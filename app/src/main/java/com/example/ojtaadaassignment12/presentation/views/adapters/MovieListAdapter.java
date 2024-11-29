package com.example.ojtaadaassignment12.presentation.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.databinding.ItemMovieGridTypeBinding;
import com.example.ojtaadaassignment12.databinding.ItemMovieListTypeBinding;
import com.example.ojtaadaassignment12.domain.models.Movie;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieDetailViewModel;
import com.example.ojtaadaassignment12.presentation.viewmodels.MovieListViewModel;
import com.example.ojtaadaassignment12.util.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends PagingDataAdapter<Movie, MovieListAdapter.MovieViewHolder> {
    private boolean isGridLayout = false;
    Picasso picasso;

    MovieListViewModel movieListViewModel;

    MovieDetailViewModel movieDetailViewModel;

    public MovieListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                @Override
                public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isGridLayout) {
            ItemMovieGridTypeBinding gridTypeBinding = ItemMovieGridTypeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MovieViewHolder(gridTypeBinding);
        } else {
            ItemMovieListTypeBinding listTypeBinding = ItemMovieListTypeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MovieViewHolder(listTypeBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        if (movie != null) {
            holder.bind(movie);

            if (!isGridLayout) {
                // Set onClickListener for favorite button in list layout
                holder.listTypeBinding.imgFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (movie.getIsFavorite() == 0) {
                            movie.setIsFavorite(1);
                            movieListViewModel.insertFavoriteMovie(movie);
                        } else {
                            movie.setIsFavorite(0);
                            movieListViewModel.deleteFavoriteMovie(movie);
                        }
                    }
                });
            }

            // Using live data to navigate to movie detail fragment
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieDetailViewModel.setMovieDetailMutableLiveData(movie);
                }
            });
        }

    }

    /**
     * Set the layout of the recycler view
     *
     * @param isGridLayout: true if grid layout, false if list layout
     */
    public void setGridLayout(boolean isGridLayout) {
        this.isGridLayout = isGridLayout;
        notifyDataSetChanged();
    }

    /**
     * Set the view model for the adapter
     *
     * @param movieListViewModel: view model for the adapter
     */
    public void setMovieListViewModel(MovieListViewModel movieListViewModel) {
        this.movieListViewModel = movieListViewModel;
    }

    /**
     * Set the view model for the adapter
     *
     * @param movieDetailViewModel: view model for the adapter
     */
    public void setMovieDetailViewModel(MovieDetailViewModel movieDetailViewModel) {
        this.movieDetailViewModel = movieDetailViewModel;
    }


    /**
     * Update the favorite icon of the movie when the favorite movie is updated
     *
     * @param movie: movie to update
     */
    public void updateItem(Movie movie) {
        List<Movie> movies = snapshot().getItems();

        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == movie.getId()) {
                Movie m = movies.get(i);
                m.setIsFavorite(movie.getIsFavorite());
                notifyItemChanged(i);
                break;
            }
        }
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public final ItemMovieListTypeBinding listTypeBinding;
        public final ItemMovieGridTypeBinding gridTypeBinding;
        public final boolean isGridLayout;

        public MovieViewHolder(ItemMovieListTypeBinding binding) {
            super(binding.getRoot());
            this.listTypeBinding = binding;
            this.gridTypeBinding = null;
            this.isGridLayout = false;
        }

        public MovieViewHolder(ItemMovieGridTypeBinding binding) {
            super(binding.getRoot());
            this.gridTypeBinding = binding;
            this.listTypeBinding = null;
            this.isGridLayout = true;
        }

        public void bind(Movie movie) {
            if (picasso == null) {
                picasso = Picasso.get();
            }

            if (isGridLayout) {
                gridTypeBinding.tvMovieTitle.setText(movie.getTitle());
                picasso.load(Constant.IMAGE_BASE_URL + movie.getPosterPath())
                        .placeholder(R.drawable.baseline_image_24)
                        .error(R.drawable.baseline_image_not_supported_24)
                        .into(gridTypeBinding.imgMoviePoster);
            } else {
                listTypeBinding.tvMovieTitle.setText(movie.getTitle());
                listTypeBinding.tvRating.setText(String.format("%.1f/10", movie.getVoteAverage()));
                listTypeBinding.tvDate.setText(movie.getReleaseDate());
                listTypeBinding.tvOverview.setText(movie.getOverview());

                picasso.load(Constant.IMAGE_BASE_URL + movie.getPosterPath())
                        .placeholder(R.drawable.baseline_image_24)
                        .error(R.drawable.baseline_image_not_supported_24)
                        .into(listTypeBinding.imgMoviePoster);

                if (movie.isAdult()) {
                    listTypeBinding.imgAdultTag.setVisibility(View.VISIBLE);
                } else {
                    listTypeBinding.imgAdultTag.setVisibility(View.GONE);
                }

                if (movie.getIsFavorite() == 0) {
                    listTypeBinding.imgFavorite.setImageResource(R.drawable.ic_star);
                } else {
                    listTypeBinding.imgFavorite.setImageResource(R.drawable.ic_star_favorite);
                }
            }
        }
    }

}
