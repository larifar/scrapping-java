package br.com.precobaixo.camera.scraping;

import java.util.Date;
import java.util.List;

import br.com.precobaixo.camera.dto.ItemGoogleDto;
import br.com.precobaixo.camera.util.ScrappingUtil;

public class OrganizadorDados {
	private List<ItemGoogleDto> lista;
	private Date data = new Date();
	
	public OrganizadorDados(){
		obterLista();
	}
	
	public void obterLista() {
		ScrappingUtil scrap = new ScrappingUtil();
		this.lista = scrap.obtemInfo();
		System.out.print("Data da busca: "  + data);
	}

}
