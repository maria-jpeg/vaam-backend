package projeto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.knowm.dropwizard.sundial.SundialConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class NCFinderConfiguration extends Configuration {
    // TODO: implement service configuration

    @Valid
    @Getter @Setter
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();


    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    //VAAM
    @Valid
    @NotNull
    public SundialConfiguration sundialConfiguration = new SundialConfiguration();

    @JsonProperty("sundial")
    public SundialConfiguration getSundialConfiguration() {

        return sundialConfiguration;
    }

}
