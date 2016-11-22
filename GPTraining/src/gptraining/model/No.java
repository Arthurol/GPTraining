package gptraining.model;

import java.util.Random;

import lombok.Getter;
import lombok.Setter;

/**
 * Estrutura das árvores sintéticas, que carrega um elemento do conjunto de terminais ou do conjunto de funções.
 */
public class No 
{
	public TipoDeNo tipoDeNo;
	private @Getter @Setter String simboloTerminal;
	private @Setter Operador operador;
	public @Getter @Setter No noFilhoEsquerda;
	public @Getter @Setter No noFilhoDireita;
	public @Getter @Setter int profundidade;
	
	public No()
	{
		this.tipoDeNo =  null;
		this.simboloTerminal = null;
		this.operador = null;
		this.profundidade = 0;
		this.noFilhoEsquerda = null;
		this.noFilhoDireita = null;
	}
	
	public No(int profundidade)
	{
		this.profundidade = profundidade;
		Random random = new Random();
		String terminalEscolhido = null;
		
		if (profundidade == 0)
		{
			this.tipoDeNo = TipoDeNo.folha;
			int numDecisaoTerminal = random.nextInt(12);
			
			
			int terminalInt = numDecisaoTerminal % 12;

			if (terminalInt > 9)
			{
				terminalEscolhido = "x";
				
			} else
			{
				boolean decisao = (random.nextInt() % 2 == 0? true : false);
				
				if (decisao == false)
					terminalInt *= -1;
				
				terminalEscolhido = String.valueOf(terminalInt);
			}
			this.noFilhoEsquerda = null;
			this.noFilhoDireita = null;
			this.operador = null;
		}
		else if (profundidade > 1)
		{
			this.tipoDeNo = TipoDeNo.interno;
			Operador operadorEscolhido = Operador.soma;
			int numDecisaoOperador = random.nextInt(4);
			this.operador = operadorEscolhido.getOperador(numDecisaoOperador);
		}
		
		this.simboloTerminal = terminalEscolhido;
	}
	
	public Operador getOperador()
	{
		return (operador.getOperador(operador.getCodigo()));
	}
}
