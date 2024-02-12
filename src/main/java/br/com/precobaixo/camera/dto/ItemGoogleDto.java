package br.com.precobaixo.camera.dto;

import java.io.Serializable;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemGoogleDto implements Serializable{

	public ItemGoogleDto(String descricao, String valor, String href) {
		this.descricao = descricao;
		this.valor = valor;
		this.endereco = href;
	}
	private static final long serialVersionUID = 1L;
	
	private String descricao;

	private String valor;
	private String endereco;
	@Nullable
	private String loja;
	
	@Override
	public String toString() {
		return "Item: " + this.descricao + ", Valor: " + this.valor + "; ";
	}

}
