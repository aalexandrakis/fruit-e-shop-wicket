package com.aalexandrakis;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

public class WhoPage extends BasePage {
	private static final long serialVersionUID = 1L;

	
	public WhoPage(final PageParameters parameters) {
		super(parameters);
        //add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

		// TODO Add your page's components here

    }
}
