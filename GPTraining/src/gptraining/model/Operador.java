package gptraining.model;

import java.util.Random;
import lombok.Getter;
import lombok.Setter;

/**
 * Operadores matem�ticos, usados nos n�s internos da �rvore para representar express�es matem�ticas.
 */
public enum Operador 
{
	soma(0),
	multiplicacao(1),
	subtracao(2),
	divisao(3);
	
	public @Getter int codigo;

	private Operador()
	{
		Random random = new Random();
		int num = random.nextInt(4);
		this.codigo = num;
	}
	
	private Operador(int codigo)
    {
        this.codigo = codigo;
    }
	
	public Operador getOperador(int codigo)
	{
		switch(codigo)
		{
		case 0:
			return Operador.soma;
		
		case 1:
			return Operador.multiplicacao;
			
		case 2:
			return Operador.subtracao;
			
		case 3:
			return Operador.divisao;			
		}
		
		return null;
	}

	public int getCodigo() {
		return codigo;
	}
	
}

