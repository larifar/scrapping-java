package br.com.precobaixo.camera.util;

import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import br.com.precobaixo.camera.dto.ItemGoogleDto;

@Component
public class ScrappingUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScrappingUtil.class);

	private static final String BASE_URL_GOOGLE = "https://www.google.com/search";
	private static final String ITEM_PESQUISA_LOJA_GOOGLE = "?q=camera+canon&tbm=shop";
	private static final String COMPLEMENTO_URL_GOOGLE = "&hl=pt-BR";

	public List<ItemGoogleDto> obtemInfo() {
		String url = BASE_URL_GOOGLE + ITEM_PESQUISA_LOJA_GOOGLE + COMPLEMENTO_URL_GOOGLE;
		List<ItemGoogleDto> lista = new ArrayList();
		Document documento = null;

		try {
			documento = Jsoup.connect(url).get();

			String title = documento.title();
			LOGGER.info("Titulo da página: {}", title);

			Elements gridResults = documento.select("div.sh-dgr__gr-auto.sh-dgr__grid-result");

			// Itere sobre os resultados
			for (Element gridResult : gridResults) {
				String href = obterLink(gridResult);
				
				if(!href.startsWith("https://www.google.com.br")) {
					String descricao = obterDescricao(gridResult);
					String valor = obterSpanValor(gridResult);
					lista.add(new ItemGoogleDto(descricao, valor, href));
					
				} else {
					Document docGoogle = Jsoup.connect(href).get();
					String descricao = obterItemGoogle(docGoogle).text();
					Elements elementos = docGoogle.select("tbody#sh-osd__online-sellers-cont").select("tr.sh-osd__offer-row");
					
					for (Element loja : elementos) {
						Elements lojaConteudo = loja.select("td.SH30Lb>div.kPMwsc").select("a.b5ycib");
						String nomeLoja = lojaConteudo.text().replace("Abre em uma nova janela", "");
						String urlLoja = decodificaUrl(lojaConteudo.attr("href").replace("/url?q=", ""));
						String valor = loja.select("td.SH30Lb>div>div.drzWO").text();
						lista.add(new ItemGoogleDto(descricao, valor, href, nomeLoja));
						
					}
				}
				
			}
			System.out.println(lista);
			System.out.println("Total de itens buscados: " + lista.size());
		} catch (Exception e) {
			LOGGER.error("ERRO AO TENTAR CONECTAR NO GOOGLE COM JSOUP -> {}", e.getMessage());
			e.printStackTrace();
		}

		return lista;
	}
	
	
	public Element obterConteudo(Element elemento) {
		return elemento.selectFirst("div.sh-dgr__content").selectFirst("span.C7Lkve");
	}
	
	public Elements obterItemGoogle(Element elemento) {
		return elemento.select("div.sg-product__dpdp-c").select("a.sh-t__title.sh-t__title-pdp.translate-content");
	}
	
	public String obterSpanValor(Element elemento) {
		Element item = elemento.selectFirst("div.sh-dgr__content").selectFirst("div.zLPF4b");
		String valor = item.selectFirst("span.a8Pemb.OFFNJ").html();
		return valor.replace("&nbsp;", "");
	}
	
	public String obterLink(Element elemento) {
		// Encontre a div de conteúdo dentro de cada resultado
		Element contentDiv = obterConteudo(elemento);

		// Encontre a tag 'a' dentro da div de conteúdo
		Element aTag = contentDiv.selectFirst("a");

		// Obtenha o atributo 'href' da tag 'a'
		String href = aTag.attr("href");
		
		if (href.startsWith("/url?url=")) {
		    // Remova a parte "/url?url=" da URL
		    href = href.replace("/url?url=", "");

		    return decodificaUrl(href);
		   
		} else if (href.startsWith("/shopping/product")){
			return "https://www.google.com.br" + tratarLinkGoogle(href);
		} else {
			return href;
		}
	} 
	
	public String obterDescricao(Element elemento) {
		Element contentDiv = obterConteudo(elemento);
		
		// Encontre a tag 'a' dentro da div de conteúdo
		Element aTag = contentDiv.selectFirst("a");

		// Encontre o h3 dentro da tag 'a'
		Element h3Tag = aTag.selectFirst("h3.tAxDx");

		// Obtenha o texto dentro da tag 'h3'
		return h3Tag.text();
		
	}
	
	public String tratarLinkGoogle(String href) {
		int index = href.indexOf("?q");
		if (index != -1) {
			return  href.substring(0, index)+ "/offers" + ITEM_PESQUISA_LOJA_GOOGLE + COMPLEMENTO_URL_GOOGLE;
		} else {
			return href;
		}
	}
	
	public String decodificaUrl(String url) {
		// Decodifique a URL
	    String decodedHref;
		try {
			decodedHref = URLDecoder.decode(url, "UTF-8");
			return decodedHref;
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}

}
