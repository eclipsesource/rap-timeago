package com.eclipsesource.internal.widgets.timeago;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;


public class Activator implements BundleActivator, ServiceTrackerCustomizer<HttpService, HttpService> {

  private static final String TIMEAGO_ALIAS = "/timeago";
  private ServiceTracker<HttpService, HttpService> tracker;
  private BundleContext context;

  public void start( BundleContext context ) throws Exception {
    this.context = context;
    tracker = new ServiceTracker<HttpService, HttpService>( context, HttpService.class.getName(), this );
    tracker.open();
  }

  public void stop( BundleContext context ) throws Exception {
    tracker.close();
    tracker = null;
    this.context = null;
  }

  public HttpService addingService( ServiceReference<HttpService> reference ) {
    HttpService httpService = context.getService( reference );
    try {
      httpService.registerResources( TIMEAGO_ALIAS, "resources", null );
    } catch( NamespaceException e ) {
      throw new IllegalStateException( "Could not register Timeago resources" );
    }
    return httpService;
  }

  public void modifiedService( ServiceReference<HttpService> reference,
                               HttpService service )
  {
  }

  public void removedService( ServiceReference<HttpService> reference,
                              HttpService service )
  {
    service.unregister( TIMEAGO_ALIAS );
  }
}
