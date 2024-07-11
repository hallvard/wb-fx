package no.hal.wb.app;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import javafx.fxml.FXMLLoader;
import no.hal.wb.fx.FxmlViewProvider;
import no.hal.wb.markdown.MarkdownViewController;
import no.hal.wb.markdown.MarkdownViewProvider;
import no.hal.wb.views.ViewManager;
import no.hal.wb.views.ViewProvider;

@ApplicationScoped
public class ViewProviders {
    
    @Inject
    Provider<FXMLLoader> fxmlLoaderProvider;

    @Produces
    ViewProvider csvView() throws IOException {
        return new FxmlViewProvider(new ViewProvider.Info("no.hal.wb.app.CsvView", "Csv", "Data"), fxmlLoaderProvider, "/no/hal/wb/fx/CsvView.fxml"){};
    }

    @Produces
    ViewProvider tableSummaryView() throws IOException {
        return new FxmlViewProvider(new ViewProvider.Info("no.hal.wb.app.TableSummaryView", "Table summary", "Data"), fxmlLoaderProvider, "/no/hal/wb/fx/TableSummaryView.fxml"){};
    }

    @Produces
    ViewProvider expressionView() throws IOException {
        return new FxmlViewProvider(new ViewProvider.Info("no.hal.wb.app.ExpressionView", "Expression", "Scripting"), fxmlLoaderProvider, "/no/hal/wb/fx/ExpressionSupportView.fxml"){};
    }

    @Produces
    ViewProvider barChartView() throws IOException {
        return new FxmlViewProvider(new ViewProvider.Info("no.hal.wb.app.BarChartView", "Bar chart", "Charts"), fxmlLoaderProvider, "/no/hal/wb/fx/BarChartView.fxml"){};
    }

    @Produces
    ViewProvider stackedChartView() throws IOException {
        return new FxmlViewProvider(new ViewProvider.Info("no.hal.wb.app.StackedBarChartView", "Stacked bar chart", "Charts"), fxmlLoaderProvider, "/no/hal/wb/fx/StackedBarChartView.fxml"){};
    }

    @Produces
    ViewProvider lineChartView() throws IOException {
        return new FxmlViewProvider(new ViewProvider.Info("no.hal.wb.app.LineChartView", "Line chart", "Charts"), fxmlLoaderProvider, "/no/hal/wb/fx/LineChartView.fxml"){};
    }

    @Inject
    jakarta.enterprise.inject.Instance<MarkdownViewController> markdownViewController;

    @Produces
    ViewProvider welcomeView() throws IOException {
        return new MarkdownViewProvider(markdownViewController, new ViewProvider.Info("no.hal.wb.app.WelcomeView", "Welcome", "Help"), ViewManager.VIEW_INFO_PATH_FORMAT) {};
    }
}
