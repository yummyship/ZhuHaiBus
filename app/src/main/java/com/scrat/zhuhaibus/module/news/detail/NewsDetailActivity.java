package com.scrat.zhuhaibus.module.news.detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.scrat.zhuhaibus.R;
import com.scrat.zhuhaibus.data.modle.News;
import com.scrat.zhuhaibus.databinding.ActivityNewsDetailBinding;
import com.scrat.zhuhaibus.framework.common.BaseActivity;
import com.scrat.zhuhaibus.framework.glide.GlideApp;
import com.scrat.zhuhaibus.framework.glide.GlideRequests;
import com.umeng.analytics.MobclickAgent;

public class NewsDetailActivity extends BaseActivity implements NewsDetailContract.View {
    private static final String ID = "id";
    public static void show(Context ctx, String newsId) {
        Intent i = new Intent(ctx, NewsDetailActivity.class);
        i.putExtra(ID, newsId);
        ctx.startActivity(i);
    }

    private NewsDetailContract.Presenter presenter;
    private ActivityNewsDetailBinding binding;
    private GlideRequests glideRequests;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glideRequests = GlideApp.with(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);
        String newsId = getIntent().getStringExtra(ID);
        new NewsDetailPresenter(this, newsId);
        presenter.loadData();
        binding.title.setOnClickListener(v -> binding.scrollView.scrollTo(0, 0));
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, "view", "NewsDetailActivity");
        MobclickAgent.onPageStart("NewsDetailActivity");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("NewsDetailActivity");
    }

    @Override
    public void setPresenter(NewsDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingData() {

    }

    @Override
    public void showLoadDataFail(int resId) {
        showMessage(resId);
    }

    @Override
    public void showData(News news) {
        binding.title.setText(news.getTitle());
        binding.newsTitle.setText(news.getTitle());
        glideRequests.load(news.getCover()).fitCenter().into(binding.newsCover);
        binding.newsDetail.fromHtml(news.getDetail());
    }
}
