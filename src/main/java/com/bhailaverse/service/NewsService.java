package com.bhailaverse.service;

import java.util.List;

import com.bhailaverse.model.NewsData;

import rx.Observable;

public interface NewsService {
	public Observable<List<NewsData>> getNews();
}
