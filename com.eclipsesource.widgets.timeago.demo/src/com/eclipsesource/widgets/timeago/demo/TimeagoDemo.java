package com.eclipsesource.widgets.timeago.demo;

import java.util.Date;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.widgets.timeago.Timeago;


public class TimeagoDemo implements IEntryPoint {

  public int createUI() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new GridLayout( 3, true ) );
    
    final Timeago timeago = new Timeago( shell, SWT.NONE );
    GridData timeagoLayoutData = new GridData( SWT.FILL, SWT.CENTER, true, false);
    timeagoLayoutData.heightHint = 15;
    timeago.setLayoutData( timeagoLayoutData );
    timeago.setCss( getClass().getResource( "/style/style.css" ) );
    timeago.setDate( new Date() );
    
    Button button = new Button( shell, SWT.PUSH );
    button.setLayoutData( new GridData( SWT.BEGINNING, SWT.CENTER, true, false ) );
    button.setText( "Apply Text to Label" );
    
    final Label label = new Label( shell, SWT.NONE );
    label.setLayoutData( new GridData( SWT.BEGINNING, SWT.CENTER, true, false ) );
    label.setText( "The text can be applied to me" );
    
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {
        label.setText( timeago.getText() );
      };
    } );
    
    
    shell.setSize( 700, 200 );
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() )
        display.sleep();
    }
    display.dispose();
    return 0;
  }
}
