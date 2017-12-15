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
	private @Getter @Setter double aptidao;
	
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
	 * @throws Exception 
	 */
	public double resolverExpressao(double valorX) throws Exception
	{
		return resolverExpressao(raiz, valorX);
	}
	
	/**
	 * Recursivamente resolve as opera��es matem�ticas da �rvore.
	 * @throws Exception 
	 */
	public double resolverExpressao(No raizSubArvore, double valorX) throws Exception
	{
		if (raizSubArvore.getNoFilhoEsquerda() != null && raizSubArvore.getNoFilhoDireita() != null)
		{
			No filhoEsquerda = raizSubArvore.getNoFilhoEsquerda();
			No filhoDireita = raizSubArvore.getNoFilhoDireita();
			Operacao operador = raizSubArvore.getOperador();
			
			if (!filhoEsquerda.possuiFilhos()  && !filhoDireita.possuiFilhos() )
			{
				return resolverOperacao(filhoEsquerda.getSimboloTerminal(), filhoDireita.getSimboloTerminal(), operador, valorX);		
			}
			
			if (filhoEsquerda.possuiFilhos() && !filhoDireita.possuiFilhos())
			{
				return resolverOperacao(String.valueOf(resolverExpressao(filhoEsquerda, valorX)), filhoDireita.getSimboloTerminal(), operador, valorX);
			}
			
			if (!filhoEsquerda.possuiFilhos() && filhoDireita.possuiFilhos())
			{
				return resolverOperacao(filhoEsquerda.getSimboloTerminal(), String.valueOf(resolverExpressao(filhoDireita, valorX)), operador, valorX);
			}
			
			if (filhoEsquerda.possuiFilhos() && filhoDireita.possuiFilhos())
			{
				return resolverOperacao(String.valueOf(resolverExpressao(filhoEsquerda, valorX)), String.valueOf(resolverExpressao(filhoDireita, valorX)), operador, valorX);
			}
		}
		throw new Exception("m�todo resolverExpress�o sendo chamado de um n� folha.");
	}
	
	/**
	 * Retorna, na forma de String, o resultado de uma opera��o entre dois terminais.
	 */
	public double resolverOperacao(String terminalEsquerda, String terminalDireita, Operacao operador, double valorX) throws Exception
	{
		double numeroEsquerda = 0.0;
		double numeroDireita = 0.0;

		numeroEsquerda = terminalEsquerda.equalsIgnoreCase("x") ? valorX : Double.valueOf(terminalEsquerda);
		numeroDireita = terminalDireita.equalsIgnoreCase("x") ? valorX : Double.valueOf(terminalDireita);
		
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
					throw new Exception ("Uma divis�oo por 0 foi encontrada. Execu��o cancelada.");
				
				return numeroEsquerda / numeroDireita;		
		}
		
		throw new Exception ("Operador invalido");
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

	public void setAptidao(double aptidao) {
		this.aptidao = aptidao;
		
	}
	
	/*
	 * Retorna a quantidade de n�s da �rvore
	 */
	public int getTamanhoArvore(No raiz)
	{
		if (raiz == null)
			return 0;
		if (raiz.getNoFilhoEsquerda() == null && raiz.getNoFilhoDireita() == null)
			return 1;
		
		return (1 + getTamanhoArvore(raiz.getNoFilhoEsquerda()) + getTamanhoArvore(raiz.getNoFilhoDireita()));
	}
	
	public int getQuantidadeOperadores(No raiz)
	{
		if (raiz.getOperador() == null)
			return 0;
		if (raiz.getNoFilhoEsquerda().getOperador() == null && raiz.getNoFilhoDireita().getOperador() == null)
			return 1;
		
		return (1 + getQuantidadeOperadores(raiz.getNoFilhoEsquerda()) + getQuantidadeOperadores(raiz.getNoFilhoDireita()));
	}
	
	public boolean checaValidadeArvore()
	{
		int tamanho = this.getTamanhoArvore(this.getRaiz());
		
		if (this.checaExistenciaDeNoX(this.getRaiz()) == false)
			return false;
		
		return true;
		//TODO Casos de teste
	}
	
	/*
	 * Verifica se n�s com opera��es possuem dois filhos e se n�s com terminais s�o folhas.
	 */
	private boolean checaCoerenciaNos(No raiz)
	{
		if (raiz.getOperador() != null)
		{
			if (raiz.getSimboloTerminal() != null || raiz.getNoFilhoEsquerda() == null || raiz.getNoFilhoDireita() == null)
				return false;
			
			return (checaCoerenciaNos(raiz.getNoFilhoEsquerda()) == true && checaCoerenciaNos(raiz.getNoFilhoDireita()) == true ? 
					true : false);
		}
		else if (!raiz.getSimboloTerminal().isEmpty() && raiz.getSimboloTerminal() != null)
		{
			if (raiz.getOperador() != null || raiz.getNoFilhoEsquerda() != null || raiz.getNoFilhoDireita() != null)
				return false;
						
			if (!raiz.getSimboloTerminal().equalsIgnoreCase("x"))
			{
				try
				{
					double d = Double.parseDouble(raiz.getSimboloTerminal());
					
				} catch (NumberFormatException nfe)
				{
					return false;
				}
			}
			return true;
		}
		return false;
		//TODO Casos de Teste
	}
	
}
