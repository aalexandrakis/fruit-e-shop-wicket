package com.aalexandrakis;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.upload.FileUpload;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;


public class AddUpdateItemPage extends BasePage {
	private static final long serialVersionUID = 1L;
	private List<String> mmList = new ArrayList<String>();
	private List<String> DisplayList = new ArrayList<String>();
	private List<String> catList = new ArrayList<String>();
	private List<Items_Category> catList1 = new ArrayList<Items_Category>();
	private Items_Category itmCat = new Items_Category();
	//private String SelectedCategory = "";
	private Boolean isDisplayable = false;
	
	Item CurrentItem = new Item();
	Integer ItemId = 0;
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	public AddUpdateItemPage(final PageParameters parameters) {
		super(parameters);
		mmList.add("Κιλό");
		mmList.add("Τεμάχιο");
		
		DisplayList.add("Display");
		
		catList1 = getCategories();
		//SelectedCategory="";
		for(int i=0;i<catList.size();i++){
			catList.add(catList1.get(i).getCategory());
		//	if (catList1.get(i).getCategoryid().equals(CurrentItem.getCategoryid())){
		//		SelectedCategory=catList1.get(i).getCategory();
		//		SelectedCategory.
		//	}
		}
		if(FruitShopSession.get().getIsAdmin()==null ||
			!FruitShopSession.get().getIsAdmin()){
			setResponsePage(HomePage.class);
		}
		
        //add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		if (!parameters.getNamedKeys().contains("ItemId")){
			add(new Label("PageHeader", "Νέο Προϊόν"));
		} else {
			add(new Label("PageHeader", "Διόρθωση Προϊόντος"));
			ItemId = parameters.get("ItemId").toInteger();
			CurrentItem = getItem(ItemId);
			if(CurrentItem.getDisplay().equals(1)){
				isDisplayable=true;
			} else {
				isDisplayable=false;
			}
		}
        Form ItemForm = new Form("ItemForm", new CompoundPropertyModel<Item>(CurrentItem));
        ItemForm.setMultiPart(true);

        add(ItemForm);
        ItemForm.add(new Label("ItemIdHeader", "Κωδικός"));
        ItemForm.add(new NumberTextField("itemid"));
        ItemForm.add(new Label("DescrHeader", "Ονοματεπώνυμο"));
        ItemForm.add(new TextField("descr").setRequired(true));
        ItemForm.add(new Label("MmHeader", "Μον.μέτρησης"));
        ItemForm.add(new RadioChoice("mm", mmList).setRequired(true));
        ItemForm.add(new Label("PriceHeader", "Τιμή"));
        ItemForm.add(new NumberTextField("price").setRequired(true));
        ItemForm.add(new Label("CategoryHeader", "Κατγορία"));
        
        itmCat = getCategory(CurrentItem.getCategoryid());
        final DropDownChoice category_cbx = new DropDownChoice(
        		"category_cbx",
        		new Model(itmCat),
        		getCategories(),
        		new ChoiceRenderer("category", "categoryid"));
        ItemForm.add(category_cbx.setRequired(true));
        //ItemForm.add(new DropDownChoice("categoryid", catList, SelectedCategory));
        
        ItemForm.add(new Label("DisplayHeader", "Εμφάνιση"));
        ItemForm.add(new CheckBox("chk_display", new Model(isDisplayable)));
        ItemForm.add(new Label("PhotoHeader", "Φωτογραφία"));
        ItemForm.add(new TextField("photo"));
        ItemForm.add(new Label("UploadPhotoHeader", "Ανεβάστε νέα φωτογραφία"));
        final FileUploadField fileUpload = new FileUploadField("fileUpload", new Model());
        ItemForm.add(fileUpload);

        
        Button SaveButton =new Button("SaveButton"){
        	@Override
        	public void onSubmit(){
        		Date date = new Date();
        		DateFormat dateFormatIsoInt = new SimpleDateFormat("yyyyMMdd");
        		//Set Current date
        		CurrentItem.setLastupdate(Integer.valueOf(dateFormatIsoInt.format(date).toString()));
        		//Set Display value from checkbox
        		if(isDisplayable){
        			CurrentItem.setDisplay(1);
    			} else {
    				CurrentItem.setDisplay(0);
    			}
        		//SetCategory from Combobox
        		CurrentItem.setCategoryid(Integer.valueOf(category_cbx.getValue()));
        		
        		//Upload Photo
        		final org.apache.wicket.markup.html.form.upload.FileUpload uploadedFile 
        		            = fileUpload.getFileUpload();
    			if (uploadedFile != null) {
    				// write to a new file
    				String UPLOAD_FOLDER = WicketApplication.get().ProductImgPath + "\\";
    				File newFile = new File(UPLOAD_FOLDER
    					+ uploadedFile.getClientFileName());
    				if (newFile.exists()) {
    					newFile.delete();
    				}
    				try {
    					newFile.createNewFile();
    					uploadedFile.writeTo(newFile);
    					info("saved file: " + uploadedFile.getClientFileName());
    				} catch (Exception e) {
    					throw new IllegalStateException("Error");
    				}
    			 }
                 //Set uploaded photo to item photo
    			CurrentItem.setPhoto(uploadedFile.getClientFileName());

    			/**************************************
        		if(!AddOrUpdateItem(CurrentItem)){
        			error("Η εγγραφή δεν ολοκληρώθηκε. Παρακαλώ προσπαθήστε αργότερα.");
        		} else {
        			PageParameters pgp = new PageParameters();
        			pgp.set("CategoryId", CurrentItem.getCategoryid());
        			setResponsePage(HomePage.class, pgp);
        		}
        		********************************/
        	}
        };
        if(ItemId.equals(0)){
        	SaveButton.add(AttributeModifier.append("value", "Εγγραφή"));
        } else {
        	SaveButton.add(AttributeModifier.append("value", "Διόρθωση"));
        }
        ItemForm.add(SaveButton);
    }
}
