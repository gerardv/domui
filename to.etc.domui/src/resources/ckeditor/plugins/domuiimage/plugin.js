﻿/*
 * vmijic 20131206 - DomUI Image plugin for CKeditor
 *  
 * This plugin register Toolbar items for command that would be used to implement custom image browsing integrated with Domui.
 * based on tutorial found at http://docs.cksource.com/CKEditor_3.x/Tutorials/Timestamp_Plugin#
 */

CKEDITOR.plugins.add( 'domuiimage',
{
	lang: 'nl,en',	
	init: function( editor )
	{
		editor.ui.addButton( 'DomUI_Image',
				{
					label: editor.lang.domuiimage.toolbar,
					command: 'insertDomUI_Image',
					icon: this.path + 'image.gif'
				} );
		
		editor.addCommand( 'insertDomUI_Image',
				{
					exec : function( editor )
					{   
						var actualId = editor.name;
					    // call domui
						WebUI.scall(actualId, "CKIMAGE", {
							_ckId : actualId
						});
					}
				});
	}
} );

/** ckeditor domuiimage plugin for DomUI helper namespace */
var CkeditorDomUIImage;
if(CkeditorDomUIImage === undefined)
	CkeditorDomUIImage = new Object();

$.extend(CkeditorDomUIImage, {
	/***
	 * Method that is exected when some image url needs to be added to editor (usually as rendered response from domui handler)
	 */
	addImage : function(ckId, imageUrl){
		//alert('addImage, id:' + ckId + ', url:' + imageUrl);
		var oEditor = WebUI.getCkEditorInstance(ckId);
		var elem = new CKEDITOR.dom.element('img', oEditor.document);
		elem.setAttribute('src', imageUrl);
		oEditor.insertElement(elem);
	},

	/***
	 * Method that is exected when image dialog is canceled (usually as rendered response from domui handler)
	 */
	cancel : function(ckName){
		var oEditor = WebUI.getCkEditorInstance(ckId);
		//all we do is to get focus back to editor
		oEditor.focus();
	}
});
