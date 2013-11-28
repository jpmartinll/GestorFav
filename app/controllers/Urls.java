package controllers;

import models.Url;
import models.Usuario;

import org.codehaus.jackson.JsonNode;

import play.mvc.Controller;
import play.mvc.Result;

public class Urls extends Controller {
	
	
	public static Result create(Long uId) {
		Result res = null;
		
		Usuario usuario = Usuario.finder.byId(uId);
		if (usuario == null) {
			res = notFound();
		}
		else {
			JsonNode input = request().body().asJson();
			Url url = new Url(input);
			url.autor = usuario;
			url.save();
			res = ok();
		}
		
		return res;
	}
	

}
