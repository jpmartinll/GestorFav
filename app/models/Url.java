package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.JsonNode;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Url extends Model {
	
	@Id
	public Long id;
	
	@Required
	public String texto;
	
	@ManyToOne
	public Usuario autor;
	
	
	public Url(JsonNode input) {
		super();
		
		this.texto = input.get("texto").asText();
		//this.autor = Usuario.finder.byId(input.get("datauser").asLong());
	}
}