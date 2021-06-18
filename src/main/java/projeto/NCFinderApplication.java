package projeto;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit5.ResourceExtension;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.knowm.dropwizard.sundial.SundialBundle;
import org.knowm.dropwizard.sundial.SundialConfiguration;
import projeto.auth.RoleBasedAuthorizer;
import projeto.auth.OAuthAuthenticator;
import projeto.auth.JwtBean;
import projeto.controller.*;
import projeto.core.*;
import projeto.core.Process;
import projeto.data.*;
import projeto.resources.*;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;


public class NCFinderApplication extends Application<NCFinderConfiguration> {

    public static void main(final String[] args) throws Exception {
       new NCFinderApplication().run( args) ;
    }


    @Override
    public String getName() {
        return "NCFinder";
    }

    private final HibernateBundle<NCFinderConfiguration> hibernate = new HibernateBundle<NCFinderConfiguration>
            ( Event.class, User.class, Role.class, Activity.class, Mould.class, Part.class, Process.class, Tag.class, ActivityUserEntry.class, Workstation.class, Dashboard.class ) {
        @Override
        public DataSourceFactory getDataSourceFactory(NCFinderConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    private final SwaggerBundle<NCFinderConfiguration> swaggerBundle = new SwaggerBundle<NCFinderConfiguration>() {
        @Override
        protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(NCFinderConfiguration configuration) {
            return configuration.swaggerBundleConfiguration;
        }
    };

    private final SundialBundle<NCFinderConfiguration> sundialBundle = new SundialBundle<NCFinderConfiguration>() {
        @Override
        public SundialConfiguration getSundialConfiguration(NCFinderConfiguration ncFinderConfiguration) {
            return ncFinderConfiguration.getSundialConfiguration();
        }
    };

    @Override
    public void initialize(final Bootstrap<NCFinderConfiguration> bootstrap) {
        // TODO: application initialization
        //bootstrap.addBundle( new MultiPartBundle() );

        bootstrap.addBundle( hibernate );
        bootstrap.addBundle( swaggerBundle );

        //VAAM
        bootstrap.addBundle(sundialBundle);
    }

    @Override
    public void run(final NCFinderConfiguration configuration,
                    final Environment environment) {

        // Register Multipart
        environment.jersey().register( MultiPartFeature.class );

        configureCors( environment );

        // DAO
        final RoleDAO roleDAO = new RoleDAO( hibernate.getSessionFactory() );
        final UserDAO userDAO = new UserDAO( hibernate.getSessionFactory(), roleDAO );
        final LogDAO logDAO = new LogDAO( hibernate.getSessionFactory() );
        final ProcessDAO processDAO = new ProcessDAO( hibernate.getSessionFactory() );
        final EventDAO eventDAO = new EventDAO( hibernate.getSessionFactory() );
        final ActivityDAO activityDAO = new ActivityDAO( hibernate.getSessionFactory() );
        final WorkstationDAO workstationDAO = new WorkstationDAO( hibernate.getSessionFactory() );
        final MouldDAO mouldDAO = new MouldDAO( hibernate.getSessionFactory() );
        final PartDAO partDAO = new PartDAO( hibernate.getSessionFactory() );
        final TagDAO tagDAO = new TagDAO( hibernate.getSessionFactory() );
        final DashboardDAO dashboardDAO = new DashboardDAO(hibernate.getSessionFactory());

        //Bean Declaration
        final UserBean userBean = new UnitOfWorkAwareProxyFactory( hibernate ).create( UserBean.class, UserDAO.class, userDAO);
        final JwtBean jwtBean = new JwtBean();
        final LogBean logBean = new LogBean( logDAO );
        final ProcessBean processBean = new UnitOfWorkAwareProxyFactory(hibernate).create(ProcessBean.class, ProcessDAO.class, processDAO);
        final ActivityBean activityBean = new ActivityBean(activityDAO);
        final MouldBean mouldBean = new MouldBean( mouldDAO );
        final PartBean partBean = new PartBean( partDAO, tagDAO );
        final ConformanceBean conformanceBean = new ConformanceBean( logBean, processBean, partBean );
        final WorkstationBean workstationBean = new WorkstationBean( workstationDAO );
        final EventBean eventBean = new EventBean( eventDAO ,activityDAO);
        final RoleBean roleBean = new RoleBean( roleDAO );
        final TagBean tagBean = new TagBean( tagDAO );
        final ResourceBean resourceBean = new ResourceBean( logBean, processBean, activityBean, userBean, workstationBean, partBean );
        final DashboardBean dashboardBean = new DashboardBean(dashboardDAO);

        //Serv Resources Declaration
        AuthenticationServ authenticationServ = new AuthenticationServ();
        UserServ userServ = new UserServ(userBean, roleBean, tagBean, processBean);
        LoginServ loginServ = new LoginServ( userBean , jwtBean);
        ActivityServ activityServ = new ActivityServ( activityBean, logBean, processBean );
        LogServ logServ = new LogServ( logBean );
        ProcessServ processServ = new ProcessServ( processBean, activityBean, userBean, workstationBean);
        WorkflowNetworkServ workflowNetworkServ = new WorkflowNetworkServ( logBean, processBean );
        ConformanceServ conformanceServ = new ConformanceServ( conformanceBean, eventBean );
        ResourceServ resourceServ = new ResourceServ( resourceBean, jwtBean );
        EventServ eventServ = new EventServ( eventBean, activityBean, processBean, mouldBean, partBean );
        MouldServ mouldServ = new MouldServ( mouldBean, partBean ,activityBean,eventBean);
        PartServ partServ = new PartServ( partBean );
        TagServ tagServ = new TagServ( tagBean );
        WorkstationServ workstationServ = new WorkstationServ( workstationBean, activityBean );
        DashboardServ dashboardServ = new DashboardServ(dashboardBean, processBean, mouldBean);

        environment.jersey().register(authenticationServ);
        environment.jersey().register(userServ);
        environment.jersey().register(loginServ);
        environment.jersey().register(activityServ);
        environment.jersey().register(logServ);
        environment.jersey().register(processServ);
        environment.jersey().register(workflowNetworkServ);
        environment.jersey().register(conformanceServ);
        environment.jersey().register(resourceServ);
        environment.jersey().register(eventServ);
        environment.jersey().register(mouldServ);
        environment.jersey().register(partServ);
        environment.jersey().register(tagServ);
        environment.jersey().register(workstationServ);
        environment.jersey().register(dashboardServ);

        //OAuth2 - Authentication and Authorization
        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<AuthUser>()
                        .setAuthenticator(new OAuthAuthenticator())
                        .setAuthorizer(new RoleBasedAuthorizer())
                        .setPrefix("Bearer")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        //If you want to use @Auth to inject a custom Principal type into your resource
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(AuthUser.class));

        //Authentication and Authorization >>> if you wanna protect another resource, add it here:
        ResourceExtension
                .builder()
                .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
                .addProvider(new AuthDynamicFeature(new OAuthCredentialAuthFilter.Builder<AuthUser>()
                        .setAuthenticator(new OAuthAuthenticator())
                        .setAuthorizer(new RoleBasedAuthorizer())
                        .setRealm("SUPER SECRET STUFF")
                        .setPrefix("Bearer")
                        .buildAuthFilter()))
                .addProvider(RolesAllowedDynamicFeature.class)
                .addProvider(new AuthValueFactoryProvider.Binder<>(AuthUser.class))
                .addResource(authenticationServ)
                .addResource(loginServ)
                .build();

    }

    private void configureCors(Environment environment) {
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    }


}
