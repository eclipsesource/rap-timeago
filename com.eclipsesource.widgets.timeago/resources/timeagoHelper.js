/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/

var setTimeagoDate = function( dateString ) {
  $( '#timeago-div' ).attr( 'title', dateString );
  $("div.timeago").timeago();
}

var setStyle = function( css ) {
  $('head').append( '<style type="text/css">' + css + '</style>' );
}

var transmitText = function() {
  var text = $.timeago( $( '#timeago-div' ).attr( 'title') );
  updateText( text );
}

