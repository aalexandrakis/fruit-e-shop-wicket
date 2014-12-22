package com.aalexandrakis;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

public class PhotoUploadPage extends BasePage {
	private static final long serialVersionUID = 1L;
    private File[] files;
    private Folder folder;
    private List<String> PhotosList = new ArrayList<String>();
    
	@SuppressWarnings("serial")
	public PhotoUploadPage(final PageParameters parameters) {
		super(parameters);
		if(FruitShopSession.get().getIsAdmin()==null ||
			!FruitShopSession.get().getIsAdmin()){
			setResponsePage(HomePage.class);
		}
		
		String UPLOAD_FOLDER = WicketApplication.get().ImagesPath + "\\";
       	Folder folder = new Folder(UPLOAD_FOLDER);
       	files = folder.listFiles();
       	for (int i=0;i<files.length;i++){
       		//PhotosList = files.
       		if (files[i].isFile()){
       			PhotosList.add(files[i].getName());
       		}
       	}
		// TODO Add your page's components here
		add(new Label("PageHeader", "Ανεβάστε φωτογραφίες"));
		Form UploadImageForm = new Form("UploadImageForm");
		add(UploadImageForm);
		UploadImageForm.add(new Label("UploadPhotoHeader", "Ανεβάστε νέα φωτογραφία"));
        final FileUploadField fileUpload = new FileUploadField("fileUpload", new Model());
        UploadImageForm.add(fileUpload);
        
        Button UploadButton =new Button("UploadButton"){
        	@Override
        	public void onSubmit(){
        		//Upload Photo
        		final org.apache.wicket.markup.html.form.upload.FileUpload uploadedFile 
        		            = fileUpload.getFileUpload();
    			if (uploadedFile != null) {
    				// write to a new file
    				String UPLOAD_FOLDER = WicketApplication.get().ImagesPath + "\\";
    				File newFile = new File(UPLOAD_FOLDER
    					+ uploadedFile.getClientFileName());
    				if (newFile.exists()) {
    					newFile.delete();
    				}
    				try {
    					newFile.createNewFile();
    					uploadedFile.writeTo(newFile);
    					setResponsePage(PhotoUploadPage.class);
    				} catch (Exception e) {
    					throw new IllegalStateException("Error");
    				}
    			 }
        	}
        };
       	UploadButton.add(AttributeModifier.append("value", "Ανέβασμα"));
       	UploadImageForm.add(UploadButton);
       	
       	add(new ListView("photolist", new Model((Serializable) PhotosList)){
			@Override
			protected void populateItem(ListItem item) {
				// TODO Auto-generated method stub
				String photo_str = (String) item.getModelObject();
				item.add(new Image("StoreImage"){}.add(AttributeModifier.replace("src", "/images/"+ photo_str)));
       			item.add(new Label("PhotoName", photo_str));
			}
       		
       	});
    }
	
	@Override
	public void onBeforeRender(){
		super.onBeforeRender();
	   	for (int i=0;i<files.length;i++){
       		//PhotosList = files.
       		if (files[i].isFile()){
       			PhotosList.add(files[i].getName());
       		}
       	}
	}
    
}
