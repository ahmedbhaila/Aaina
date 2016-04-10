package com.bhailaverse.service;

import com.bhailaverse.model.NewsData;

import rx.Observable;

public interface NewsService {
	public Observable<NewsData> getNews();
}
