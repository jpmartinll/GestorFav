package controllers;

import java.util.List;

import models.Url;
import models.Usuario;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Document;

import play.i18n.Messages;
import play.libs.Json;
import play.mvc.*;

public class Usuarios extends Controller {
	
	private static JsonNode errorJson(Integer code, String message) {
		ObjectNode node = Json.newObject();
		node.put("code", code);
		node.put("message", Messages.get(message));
		return node;
	}
	
	private static Result okWithJsonContent(Content c) {
		response().setContentType("application/json");
		return ok(c);
	}
	
	private static boolean acceptsJson() {
		return request().accepts("application/json");
	}
	
	private static boolean acceptsXml() {
		return (request().accepts("application/xml") || request().accepts("text/xml"));
	}

	private static boolean contentIsJson() {
		String content = request().getHeader("Content-Type"); 
		return content.startsWith("application/json");
	}
	
	private static boolean contentIsXml() {
		String content = request().getHeader("Content-Type"); 
		return (content.startsWith("application/xml") || content.startsWith("text/xml"));
	}

	public static Result index() {
		Result res;

		List<Usuario> lista = Usuario.findPagina();
		if (acceptsJson()) {
			res = okWithJsonContent(views.txt.usuarios.render(lista));
		}
		else if (acceptsXml()) {
			res = ok(views.xml.usuarios.render(lista));
		}
		else {
			res = badRequest(errorJson(1, "unsupported_format"));
		}
			
		return res; 
	}
	
	public static Result retrieve(Long uId) {
		Result res;
		
		Usuario usuario = Usuario.finder.byId(uId);
		if (usuario == null) {
			res = notFound();
		}
		else {
			if (acceptsJson()) {
				res = okWithJsonContent(views.txt._usuario.render(usuario));
			}
			else if (acceptsXml()) {
				res = ok(views.xml._usuario.render(usuario));
			}
			else {
				res = badRequest(errorJson(1, "unsupported_format"));
			}
		}
		
		return res;
	}
	
	private static Usuario getUsuarioFromBody() {
		Usuario usuario;
		
		if (contentIsJson()) {
			JsonNode input = request().body().asJson();
			usuario = new Usuario(input);
		}
		else if (contentIsXml()) {
			Document input = request().body().asXml();
			usuario = new Usuario(input);
		}
		else {
			usuario = null;
		}
		
		return usuario;
	}

	public static Result create() {
		Result res = ok();
		Usuario usuario = getUsuarioFromBody();
		if (usuario == null) {
			res = badRequest(errorJson(1, "unsupported_format"));
		}
		else {
			List<String> errors = usuario.validateAndSave();
			if (errors.size() == 0) {
				response().setHeader(LOCATION, routes.Usuarios.retrieve(usuario.id).absoluteURL(request()));
				res = ok();
			}
			else {
				res = badRequest();
			}
		}
		
		return res;
		
	}

	public static Result update(Long uId) {
		Result res = null;
		
		Usuario usuario = Usuario.finder.byId(uId);
		if (usuario == null) {
			res = notFound();
		}
		else {
			Usuario newUsuario = getUsuarioFromBody();
			if (newUsuario == null) {
				res = badRequest(errorJson(1, "unsupported_format"));
			}
			else {
				if (usuario.changeData(newUsuario)) {
					usuario.save();
					res = ok();
				}
				else {
					res = status(NOT_MODIFIED);
				}
			}
		}
		
		return res;
	}
	
	public static Result delete(Long uId) {
		Result res = null;
		
		Usuario usuario = Usuario.finder.byId(uId);
		if (usuario == null) {
			res = notFound();
		}
		else {
			usuario.delete();
			res = ok();
		}

		return res;
	}

	public static Result urls(String nom) {
		Result res = null;
		
		Usuario usuario = Usuario.finder.where().eq("nombre", nom).findUnique();
		List<Url> lista = usuario.urls;
		
		res = ok(views.txt.urls.render(lista));
		
		
		return res;
	}
}
