/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package com.eclipsesource.widgets.timeago;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;


public class Timeago extends Composite {

  private static final long serialVersionUID = 1L;
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  private static final String UPDATE_TEXT_FUNCTION = "updateText";
  private static final String TIMEAGO_URL = "/timeago/timeago.html";
  
  private Date date;
  private Browser browser;
  private boolean isBrowserReady;
  private boolean needToFlush;
  private URL cssResource;
  private String text;

  public Timeago( Composite parent, int style ) {
    super( parent, style );
    setLayout( new FillLayout() );
    browser = new Browser( this, SWT.NONE );
    browser.setUrl( TIMEAGO_URL );
    addProgressLsitenerToBrowser();
  }

  private void addProgressLsitenerToBrowser() {
    browser.addProgressListener( new ProgressListener() {
      
      public void completed( ProgressEvent event ) {
        isBrowserReady = true;
        createBrowserFunction();
        if( needToFlush ) {
          flush();
        }
      }     
      public void changed( ProgressEvent event ) {
        // do nothing
      }
    } );
  }
  
  private void createBrowserFunction() {
    new BrowserFunction( browser, UPDATE_TEXT_FUNCTION ) {

      @Override
      public Object function( Object[] arguments ) {
        text = ( String )arguments[ 0 ];
        return null;
      }
    };
  }
  
  private void flush() {
    needToFlush = false;
    String styleScript = createStyleScript();
    String dateScript = createDateScript();
    browser.evaluate( dateScript + styleScript );
  }

  public void setDate( Date date ) {
    this.date = date;
    if( isBrowserReady ) {
      String dateScript = createDateScript();
      browser.evaluate( dateScript );
    } else {
      needToFlush = true;
    }
  }

  private String createDateScript() {
    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
    String script = "setTimeagoDate('" + format.format( date ) + "');";
    return script;
  }

  public void setCss( URL cssResource ) {
    this.cssResource = cssResource;
    if( isBrowserReady ) {
      String styleScript = createStyleScript();
      browser.evaluate( styleScript );
    } else {
      needToFlush = true;
    }
  }

  private String createStyleScript() {
    StringBuilder builder = new StringBuilder();
    readFromStream( builder );
    String css = builder.toString().replaceAll( "\n", "" );
    return "setStyle('" + css + "');";
  }

  private void readFromStream( StringBuilder builder ) {
    try {
      InputStreamReader reader = new InputStreamReader( cssResource.openStream() );
      int next;
      while( ( next = reader.read() ) != -1 ) {
        builder.append( ( char )next );
      }
      reader.close();
    } catch( IOException e ) {
      throw new IllegalArgumentException( "Could not read resource with URL: " + cssResource.toString() );
    }
  }
  
  public String getText() {
    browser.evaluate( "transmitText();" );
    return text;
  }
}
