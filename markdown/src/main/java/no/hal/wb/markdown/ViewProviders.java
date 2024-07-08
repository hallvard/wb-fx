package no.hal.wb.markdown;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import no.hal.wb.views.ViewManager;
import no.hal.wb.views.ViewProvider;

@ApplicationScoped
public class ViewProviders {
    
    @Inject
    ViewManager viewManager;

    @Produces
    PathResolver pathResolver() {
        return viewManager::resolvePath;
    }

    @Inject
    Instance<MarkdownViewProvider> markdownViewProvider;

    @Produces
    ViewProvider markdownView() {
        return markdownViewProvider.get();
    }
}
