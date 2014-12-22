package com.aalexandrakis;


import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;


public class PhotosPage extends BasePage {
	private static final long serialVersionUID = 1L;
	private Folder folder = new Folder(WicketApplication.get().ImagesPath+"\\");
	private java.io.File[] files;
	private Integer CurrentImage=0;
	private Image MainPhoto = new Image("MainPhoto"){};
	WebPage ThisPage = this;

	public PhotosPage(final PageParameters parameters) {
		super(parameters);
		files =  folder.listFiles();
		
        Link PreviousPhotoLink = new Link("PreviousPhotoLink"){
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				this.getParent().get("NextPhotoLink").setVisible(true);
				if (files!=null){
					CurrentImage -= 1;
					if (files[CurrentImage].isFile()){
						MainPhoto.add(AttributeModifier.replace("src",
								"/images/"+ files[CurrentImage].getName()));
					}
				}
				if(CurrentImage.equals(0)) {
					this.setVisible(false);
				} else {
					this.setVisible(true);
				}
			}
        };
        PreviousPhotoLink.add(new Image("PreviousPhoto", 
        			new SharedResourceReference(PhotosPage.class, "buttons/previousarrow.png")));
        
        Link NextPhotoLink = new Link("NextPhotoLink"){
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				//ThisPage.get(PreviousPhotoLink).setVisible(true);
				this.getParent().get("PreviousPhotoLink").setVisible(true);
				if (files!=null){
					    CurrentImage += 1;
						if (files[CurrentImage].isFile()){
							MainPhoto.add(AttributeModifier.replace("src",
									"/images/"+ files[CurrentImage].getName()));
						}
				}
				if(CurrentImage.equals(files.length-1)) {
					this.setVisible(false);
				} else
					this.setVisible(true);
			}
        };
        
        NextPhotoLink.add(new Image("NextPhoto", 
    			new SharedResourceReference(PhotosPage.class, "buttons/nextarrow.png")));
        
        
		// TODO Add your page's components here
        
        add(PreviousPhotoLink.setVisible(false));
        add(MainPhoto);
        	MainPhoto.add(AttributeModifier.replace("src",
        			"/images/"+ files[CurrentImage].getName()));
        add(NextPhotoLink);
    }
}
