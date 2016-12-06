package gptraining.model;

import lombok.Getter;
import lombok.Setter;
import operacoes.Comparable;

/**
 * Classe que abriga algumas opera��es referente �s �rvores sint�ticas e aos n�s.
 */
public class ArvoreExpressao implements Comparable<ArvoreExpressao>
{
	private @Getter @Setter No raiz;
	private @Getter @Setter int aptidao;
	
	public ArvoreExpressao(No raiz)
	{
		this.raiz = raiz;
		this.aptidao = 0;
	}
	
	public ArvoreExpressao()
	{
		this.raiz = new No();
		this.aptidao = 0;
	}
	
	/**
	 * Recursivamente resolve as opera��es matem�ticas da �rvore, representada pelo n� raiz, retornando um resultado double convertido em String. 
	 * Como h� recurs�o, o tipo de retorno escolhido foi String, pois al�m de inteiros entre -9 e 9 pode haver x no s�mbolo de um n�.
	 */
	public double resolverExpressao(double valorX)
	{
		return resolverExpressao(raiz, valorX);
	}
	
	/**
	 * Recursivamente resolve as opera��es matem�ticas da �rvore, representada pelo n� raiz, retornando um resultado double convertido em String. 
	 * Como h� recurs�o, o tipo de retorno escolhido foi String, pois al�m de inteiros entre -9 e 9 pode haver x no s�mbolo de um n�.
	 */
	public double resolverExpressao(No raizSubArvore, double valorX)
	{
		if (raizSubArvore.getNoFilhoEsquerda() != null && raizSubArvore.getNoFilhoDireita() != null)
		{
			No filhoEsquerda = raizSubArvore.getNoFilhoEsquerda();
			No filhoDireita = raizSubArvore.getNoFilhoDireita();
			Operacao operador = raizSubArvore.getOperador();
			
			if (filhoEsquerda.possuiFilhos() == false && filhoDireita.possuiFilhos() == false)
			{
				return resolverOperacao(filhoEsquerda.getSimboloTerminal(), filhoDireita.getSimboloTerminal(), operador, valorX);		
			}
			
			if (filhoEsquerda.possuiFilhos() && filhoDireita.possuiFilhos() == false)
			{
				return resolverOperacao(resolverExpressao(filhoEsquerda, valorX), filhoDireita.getSimboloTerminal(), operador, valorX);
			}
			
			if (filhoEsquerda.possuiFilhos() == false && filhoDireita.possuiFilhos())
			{
				return resolverOperacao(filhoEsquerda.getSimboloTerminal(), resolverExpressao(filhoDireita, valorX), operador, valorX);
			}
			
			if (filhoEsquerda.possuiFilhos() && filhoDireita.possuiFilhos())
			{
				return resolverOperacao(resolverExpressao(filhoEsquerda, valorX), resolverExpressao(filhoDireita, valorX), operador, valorX);
			}
		}
		return null;
	}
	
	/**
	 * Retorna, na forma de String, o resultado de uma opera��o entre dois terminais.
	 */
	public double resolverOperacao(String terminalEsquerda, String terminalDireita, Operacao operador, double valorX) throws Exception
	{
		double numeroEsquerda = Double.valueOf(terminalEsquerda);
		double numeroDireita = Double.valueOf(terminalDireita);
		
		if (checaCaracterValido(terminalEsquerda, terminalDireita) == false)
			throw new Exception ("O n� cont�m um terminal que n�o � um inteiro entre -9 e 9 e n�o � igual a x.");
			
		if (terminalEsquerda == "x" || terminalEsquerda == "X")
			numeroEsquerda = valorX;
		
		if (terminalDireita == "x" || terminalDireita == "X")
			numeroDireita = valorX;
		
		switch (operador)
		{
			case Soma:
				return numeroEsquerda + numeroDireita;
				
			case Subtracao:
				return numeroEsquerda - numeroDireita;
				
			case Multiplicacao:
				return numeroEsquerda * numeroDireita;
				
			case Divisao:
				if (numeroDireita == 0.0)
					throw new Exception ("Uma divis�o por 0 foi encontrada. Execu��o cancelada.");
				
				return numeroEsquerda / numeroDireita;		
		}
		
		throw new Exception ("Operador invalido");
	}
	
	public boolean checaCaracterValido(String terminalEsquerda, String terminalDireita)
	{
		if ((Double.valueOf(terminalEsquerda) < -9.0 || Double.valueOf(terminalEsquerda) > 9.0 || 
				Double.valueOf(terminalDireita) < -9.0 || Double.valueOf(terminalEsquerda) > 9.0 ) && 
					(terminalEsquerda != "x" || terminalEsquerda != "X" || terminalDireita != "x" || terminalDireita != "X" ))
		return false;
		
		else
			return true;
	}
	
	/**
	 * Garante a presen�a do terminal "x" na �rvore, pois a mesma deve estar em fun��o de "x".
	 */
	public boolean checaExistenciaDeNoX(No raiz)
	{
		if (raiz.getSimboloTerminal() != null && (raiz.getSimboloTerminal().equals("x") || raiz.getSimboloTerminal().equals("X")))
			return true;
		else 
		{
			if (raiz.getNoFilhoEsquerda() != null)
			{
				if (checaExistenciaDeNoX(raiz.getNoFilhoEsquerda()))
					return true;
			}
			if (raiz.getNoFilhoDireita() != null)
			{
				if (checaExistenciaDeNoX(raiz.getNoFilhoEsquerda()))
					return true;
			}
			
			return false;
		}
	}
	
	/**
	 * Se a express�o contida na �rvore resulta em Y, dado seu valor de X correspondente, h� um ganho na aptid�o da �rvore/express�o em quest�o.
	 */
	public double avaliarAptidaoArvore(No raiz, int[] vetX, int[] vetY)
	{
		int aptidao = 0;
		for (int i = 0; i < vetX.length; i++)
		{
			if (vetY[i] == resolverExpressao(raiz, vetX[i]))
					aptidao ++;		// TODO rever
		}
		
		return aptidao;
	}
	
	public int getProfundidade()
	{
		if (raiz.getNoFilhoEsquerda() == null && raiz.getNoFilhoDireita() == null)
			return 0;
		
		if (raiz.noFilhoEsquerda.possuiFilhos() == false && raiz.noFilhoDireita.possuiFilhos() == false)
			return 1;
		
		else 
		{
			ArvoreExpressao subArvoreEsquerda = new ArvoreExpressao(raiz.getNoFilhoEsquerda());
			ArvoreExpressao subArvoreDireita = new ArvoreExpressao(raiz.getNoFilhoDireita());

			return Math.max(1 + subArvoreEsquerda.getProfundidade(), 1 + subArvoreDireita.getProfundidade());
		}
		
	}
	
	public int compareTo(ArvoreExpressao outraArvore)
	{
		if (this.aptidao < outraArvore.aptidao)
			return -1;
		
		if (this.aptidao > outraArvore.aptidao)
			return 1;
			
		return 0;
	}

	public No getRaiz() {
		return this.raiz;
	}
	
	public void setRaiz(No raiz) {
		this.raiz = raiz;
	}

	public void setAptidao(int aptidao) {
		this.aptidao = aptidao;
		
	}
}
