package quickbit.core.service;

import quickbit.core.model.NewsModel;

import java.util.List;
import java.util.Set;

public interface NewsService {

    List<NewsModel> getNews();

    void updateNews();
}
