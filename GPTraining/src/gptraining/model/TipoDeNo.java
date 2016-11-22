package gptraining.model;

import java.util.Random;

import lombok.Getter;
import lombok.Setter;

public enum TipoDeNo 
{
	interno(1),
	folha(0);
	
	private @Getter int codigo;

	private TipoDeNo(int codigo)
	{
		this.codigo = codigo;
	}
	
}
