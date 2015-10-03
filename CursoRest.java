package edu.tnt.exemplo.jsf.escolatnt.rest;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import edu.tnt.exemplo.jsf.escolatnt.business.GerenciadorDeCursos;
import edu.tnt.exemplo.jsf.escolatnt.business.bo.GerenciadorDeCursosBO;
import edu.tnt.exemplo.jsf.escolatnt.business.model.Curso;
import edu.tnt.exemplo.jsf.escolatnt.managedbeans.CursoMB;
import edu.tnt.exemplo.jsf.escolatnt.servicefacade.CrudSessionBean;
import edu.tnt.exemplo.jsf.escolatnt.servicefacade.CrudSessionFaçade;


@Path("/cursos")
public class CursoRest {
	@Resource(name = "jdbc/EscolaJsfDS", type = javax.sql.DataSource.class, mappedName = "java:/EscolaJsfDS")
	private DataSource dataSource;
	//private CrudSessionBean sessionFaçade;
	
	
    public final static  String DRIVER = "org.postgresql.Driver";
    public final static  String URL = "jdbc:postgresql://localhost/escolajsf";
    public final static  String USER = "postgres";
    public final static  String PASSWD = "admin";
    
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String selectAll() throws Exception  {
    List<Curso> cursosList = new ArrayList<Curso>();
    Connection conn = null;    
    conn = DriverManager.getConnection(URL,USER,PASSWD);
	GerenciadorDeCursos bo = new GerenciadorDeCursosBO();
	cursosList = bo.listarCursos(conn);

	String cursos = "";
	
	for (int i = 0; i < cursosList.size()  ; i++) { 
			cursos = cursos + "{'nome': '"+ cursosList.get(i).getNome() +"','codigo': '"+ cursosList.get(i).getCodigo() +"'},";
	}
	cursos = cursos.substring(0, cursos.length() -1);
		
	//String cursos = "{'name': 'Curso 1','ementa': 'Curso XXX','carga':16},{'name': 'Curso 2','ementa': 'Curso ZZZ','carga':16}";
	return cursos;
	}
	
	
	@GET
	@Path("/inserir")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String insertCurso(@Context UriInfo uriInfo ) throws Exception {
		Connection conn = null;    
	    conn = DriverManager.getConnection(URL,USER,PASSWD);
		GerenciadorDeCursos bo = new GerenciadorDeCursosBO();

	String nome = uriInfo.getQueryParameters().getFirst("nome");
	String codigo =  uriInfo.getQueryParameters().getFirst("codigo");

	 Curso cursoClasse = new Curso();
	 cursoClasse.setCodigo(codigo);
	 cursoClasse.setNome(nome);
	 try{
	   bo.inserirCurso(conn, cursoClasse);
	 }catch (Exception e){
		 return "Erro ao Inserir Curso";
	 }

	return "Inserido com Sucesso";

	}
}
