package com.yarullin.roman.gitapp.di.component;

import com.yarullin.roman.gitapp.di.module.AppModule;
import com.yarullin.roman.gitapp.di.module.DataModule;
import com.yarullin.roman.gitapp.di.module.PersistenceModule;
import com.yarullin.roman.gitapp.features.search.model.SearchRepoViewModel;
import com.yarullin.roman.gitapp.interactor.base.BaseUseCase;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        DataModule.class,
        PersistenceModule.class
})
public interface AppComponent {
    void inject(BaseUseCase useCase);
    void inject(SearchRepoViewModel searchRepoViewModel);
}
