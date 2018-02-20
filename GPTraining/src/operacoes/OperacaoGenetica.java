package operacoes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import gptraining.model.ArvoreExpressao;
import gptraining.model.CalculadorFitness;
import gptraining.model.Dataset;
import gptraining.model.No;
import gptraining.model.Operacao;
import gptraining.model.Populacao;

/*
 * Classe respons�vel pela modifica��o das �rvores de express�o atrav�s dos operadores gen�ticos.
 */
public class OperacaoGenetica {
	
	private int contadorOperacoesCrossover;
	private int contadorNosMutacao;
	private static double probabilidadeCrossover = 0.85;
	
	public OperacaoGenetica()
	{
		contadorOperacoesCrossover = 0;
		contadorNosMutacao = 0;
	}
	
	/**
	 * Crossover onde peda�os de duas �rvores de express�o s�o usados para criar um descendente. Um ponto de crossover � escolhido nas duas �rvores
	 * 
	 */
	public ArvoreExpressao subtreeCrossover(ArvoreExpressao arvoreA, ArvoreExpressao arvoreB, Random random)
	{
		//Seleciona um ponto aleat�rio de crossover nas duas �rvores
		int pontoCrossoverA = random.nextInt(arvoreA.getQuantidadeOperadores(arvoreA.getRaiz()));
		int pontoCrossoverB = random.nextInt(arvoreB.getQuantidadeOperadores(arvoreB.getRaiz()));
		No subarvoreA = quebraNoPontoCrossover(arvoreA.getRaiz(), pontoCrossoverA);
		contadorOperacoesCrossover = 0;
		No subarvoreB = quebraNoPontoCrossover(arvoreB.getRaiz(), pontoCrossoverB);

		ArvoreExpressao ArvoreA = new ArvoreExpressao(subarvoreA);
		ArvoreExpressao ArvoreB = new ArvoreExpressao(subarvoreB);
		
		//Sorteio aleat�rio para decidir qual das ra�zes das duas subarvores ser� a raiz da �rvore originada
		int sorteio = random.nextInt(2);
		ArvoreExpressao arvoreCombinada = new ArvoreExpressao();
		
		//System.out.println("\n�rvores selecionadas para o crossover: \nArvore A: " + arvoreA.stringExpressao(arvoreA.getRaiz()));
		//System.out.println("Arvore B: " + arvoreB.stringExpressao(arvoreB.getRaiz()));
		
		if (sorteio == 0)
			arvoreCombinada = combina(ArvoreA, ArvoreB);
		else
			arvoreCombinada = combina(ArvoreB, ArvoreA);
		
		//Se a �rvore gerada no crossover n�o possuir um terminal x, o crossover � feito novamente entre as duas �rvores iniciais.
		if (!arvoreCombinada.checaExistenciaDeNoX(arvoreCombinada.getRaiz()))
		{
			System.out.println("Inexist�ncia de terminal x na �rvore descendente");
			contadorOperacoesCrossover = 0;
			return null;
		}
		
		//System.out.println("�rvore descendente (A x B): " + arvoreCombinada.stringExpressao(arvoreCombinada.getRaiz()));
		contadorOperacoesCrossover = 0;
		return arvoreCombinada;
	}
	
	/**
	 * M�todo que realiza uma muta��o de ponto(point mutation)
	 */
	public ArvoreExpressao mutacao(ArvoreExpressao arvore, Random random)
	{
		ArvoreExpressao arvoreMutada = new ArvoreExpressao(arvore.getRaiz());
		int quantidadeNos = arvore.getTamanhoArvore(arvore.getRaiz());
		int pontoMutacao = random.nextInt(quantidadeNos);
		mutacaoPontoAleatorio(arvoreMutada.getRaiz(), pontoMutacao, random);
		arvoreMutada.setAptidao(0);
		arvoreMutada.setExpressao(arvoreMutada.stringExpressao(arvoreMutada.getRaiz()));
		contadorNosMutacao = 0;
		return arvoreMutada;
	}
	
	/**
	 * M�todo que percorre a �rvore at� encontrar o n� representado pelo ponto de muta��o. Ao ach�-lo, realiza sua substitui��o 
	 * por um elemento aleat�rio equivalente (operador por operador e terminal por terminal)
	 */
	public boolean mutacaoPontoAleatorio(No noRaiz, int pontoMutacao, Random random) 
	{
		if (noRaiz == null)
			return false;
		
	    if (this.contadorNosMutacao == pontoMutacao)
	    {	
	    	if (noRaiz.getOperador() != null)
	    	{
	    		No novoOperador = new No();
	    		novoOperador.preenchimentoAleatorioOperador(random);
	    		noRaiz.setOperador(novoOperador.getOperador());
	    		this.contadorNosMutacao = 0;
	    		return true;
	    	}
	    	else if (noRaiz.getSimboloTerminal() != null)
	    	{
	    		No novoTerminal = new No();
	    		novoTerminal.preenchimentoAleatorioTerminal(random);
	    		noRaiz.setSimboloTerminal(novoTerminal.getSimboloTerminal());
	    		this.contadorNosMutacao = 0;
	    		return true;
	    	}
	    	else
	    		return false;    		
	    }
	   
	    if (this.contadorNosMutacao != pontoMutacao) 
	    	this.contadorNosMutacao++;
	    
	    if (!mutacaoPontoAleatorio(noRaiz.getNoFilhoEsquerda(), pontoMutacao, random))
	    {
	    	return mutacaoPontoAleatorio(noRaiz.getNoFilhoDireita(), pontoMutacao, random);
	    }

		return true;
	}

	/**
	 * Expectativa: M�todo que chamar� o c�lculo de fitness sobre uma gera��o (List<ArvoreExpressao>) e a ordenar� pela fitness.
	 * Ap�s ocorrer� o sorteio para determinar se algum operador gen�tico (crossover ou muta��o) ser� aplicado sobre os indiv�duos de melhor fitness.
	 * � importante lembrar que o crossover deve ter uma probabilidade de ocorrer bem maior do que a muta��o.
	 */
	public Populacao selecao(Populacao populacao, Dataset dataset, Random random)
	{
		CalculadorFitness calculador = new CalculadorFitness();
		
		//faz o calculo do fitness de toda a popula��o		
		for (int i = 0; i < populacao.getIndividuos().size(); i++)
		{
			double aptidao = calculador.calcula(populacao.getIndividuos().get(i), dataset);
			populacao.getIndividuos().get(i).setAptidao(aptidao == -1 ? 1000000 : aptidao);
		}
		
		//Ordena a popula��o pela aptid�o (fitness). Quanto mais pr�xima de zero for a aptid�o, melhor � a �rvore (Dist�ncia entre dois pontos)
		Collections.sort(populacao.getIndividuos());
		
		Populacao proximaGeracao = new Populacao();
		proximaGeracao.setNumeroGeracao(populacao.getNumeroGeracao() + 1);
		int tamanhoPopulacao = populacao.getIndividuos().size();
		double tamanhoElite = (tamanhoPopulacao * 0.3);
		for (int i = 0; i < tamanhoElite; i++)
		{
			System.out.println("Arvore elite " + (i+1) + ": "  + populacao.getIndividuos().get(i).stringExpressao(populacao.getIndividuos().get(i).getRaiz()) + "----------->" + populacao.getIndividuos().get(i).getAptidao());
			proximaGeracao.adicionaIndividuo(populacao.getIndividuos().get(i));	
		}

		while(proximaGeracao.getIndividuos().size() < populacao.getIndividuos().size())
		{
			/*
			System.out.println("\n\nGera��o: Estado atual:");
			for (int i = 0; i < proximaGeracao.getIndividuos().size(); i++)
			{
				System.out.println("Arvore " + (i+1) + ": "  + proximaGeracao.getIndividuos().get(i).stringExpressao(proximaGeracao.getIndividuos().get(i).getRaiz()) + "----------->" + proximaGeracao.getIndividuos().get(i).getAptidao());
			}
			System.out.println("\n\n");
			*/
			
			double escolhaOperadorGenetico = random.nextDouble();
			ArvoreExpressao arvoreProduto = new ArvoreExpressao();
			
			if (escolhaOperadorGenetico < probabilidadeCrossover)
			{
				while(true)
				{
					List<ArvoreExpressao> candidatosA = selecaoPorTorneio(populacao, random);
					List<ArvoreExpressao> candidatosB = selecaoPorTorneio(populacao, random);
					int paiA = random.nextInt(candidatosA.size());
					int paiB = random.nextInt(candidatosB.size());
					
					ArvoreExpressao arvoreA = new ArvoreExpressao(candidatosA.get(paiA).getRaiz());
					ArvoreExpressao arvoreB = new ArvoreExpressao(candidatosB.get(paiB).getRaiz());
					
					try
					{
						if (arvoreA == null || arvoreB == null)
						{
							throw new Exception("Um dos selecionados para o crossover � null");
						}
					
						arvoreA.setAptidao(calculador.calcula(arvoreA, dataset) == -1 ? 1000000 : calculador.calcula(arvoreA, dataset));
						arvoreB.setAptidao(calculador.calcula(arvoreB, dataset) == -1 ? 1000000 : calculador.calcula(arvoreB, dataset));
						
						//compararEstruturaNos com problema de null pointer
						
						//arvoreProduto = new ArvoreExpressao(subtreeCrossover(arvoreA, arvoreB, random));
						arvoreProduto = new ArvoreExpressao(subtreeCrossover(arvoreA, arvoreB, random).getRaiz());

						if (arvoreProduto != null)
						{
							if (arvoreProduto.checaValidadeArvore(arvoreProduto.getRaiz()) && arvoreProduto.checaExistenciaDeNoX(arvoreProduto.getRaiz()))
							{
								break;
							}
						}
					} catch (Exception e)
					{
						System.out.println("Exce��o no crossover entre as �rvores:");
						System.out.println(arvoreA.stringExpressao(arvoreA.getRaiz()) + " e " + arvoreB.stringExpressao(arvoreB.getRaiz()));
					}
				}
			}
			else //sorteio determinou que o operador gen�tico ser� muta��o
			{
				while(true)
				{
					List<ArvoreExpressao> candidatosMutacao = selecaoPorTorneio(populacao, random);
					//arvoreProduto = new ArvoreExpressao(mutacao(candidatosMutacao.get(random.nextInt(candidatosMutacao.size())), random));
					arvoreProduto = new ArvoreExpressao(mutacao(candidatosMutacao.get(random.nextInt(candidatosMutacao.size())), random).getRaiz());
					
					if (arvoreProduto.checaValidadeArvore(arvoreProduto.getRaiz()) && arvoreProduto.checaExistenciaDeNoX(arvoreProduto.getRaiz()))
						break;
				}
			}		
			proximaGeracao.adicionaIndividuo(arvoreProduto);	
		}
	
		/* 
		for (ArvoreExpressao arv : proximaGeracao.getIndividuos())
		{
			double aptidao = calculador.calcula(arv, dataset);
			arv.setAptidao(aptidao == -1 ? 1000000 : aptidao);
		}
		*/
		
		for (int i = 0; i < proximaGeracao.getIndividuos().size(); i++)
		{
			double aptidao = calculador.calcula(proximaGeracao.getIndividuos().get(i), dataset);
			proximaGeracao.getIndividuos().get(i).setAptidao(aptidao == -1 ? 1000000 : aptidao);
		}
		
		Collections.sort(proximaGeracao.getIndividuos());

		System.out.println("\n\n <GERA��O " + String.valueOf(proximaGeracao.getNumeroGeracao())  + "-------------------------------------------------->");
		for (ArvoreExpressao arvoreExpressao : proximaGeracao.getIndividuos())
		{
			System.out.println(arvoreExpressao.stringExpressao(arvoreExpressao.getRaiz()) + " --> " + arvoreExpressao.getAptidao());
		}
		
		return proximaGeracao;
	}
	
	
	public No quebraNoPontoCrossover(No no, int pontoCrossover)
	{
		if (no.getOperador() == null)
			return null;
		
	    if ((this.contadorOperacoesCrossover == pontoCrossover) && no.getOperador() != null)
	    	return new No(no);
	    
	    if ((this.contadorOperacoesCrossover != pontoCrossover) && no.getOperador() != null )
	    	this.contadorOperacoesCrossover++;
	    
	    No noQuebra = quebraNoPontoCrossover(no.getNoFilhoEsquerda(), pontoCrossover);
	    if (noQuebra == null)
	    	noQuebra = quebraNoPontoCrossover(no.getNoFilhoDireita(), pontoCrossover);
	    
	    return noQuebra;
    }
	
	/**
	 * Substitui aleatoriamente um dos n�s filhos da subarvore A pela subarvore B e retorna a �rvore resultante.
	 */
	public ArvoreExpressao combina(ArvoreExpressao subarvoreA, ArvoreExpressao subarvoreB)
	{
		ArvoreExpressao arvoreResultado = new ArvoreExpressao(subarvoreA.getRaiz());
		arvoreResultado.getRaiz().setNoFilhoEsquerda(subarvoreB.getRaiz());
			
		return arvoreResultado;
	}
	
	private List<ArvoreExpressao> selecaoPorTorneio(Populacao populacao, Random random)
	{
		double escolhaGrupo = random.nextDouble();
		List<ArvoreExpressao> grupoTorneio = new ArrayList<ArvoreExpressao>();
		
		
		//Torneio formado pela metade da popula��o. Subdivis�o dessa metade em cinco grupos de quantidade igual de indiv�duos. 
		//A probabilidade de escolha de cada grupo � a seguinte: g1 - 40%, g2 - 20%, g3 - 20%, g4 - 10, g5 - 10%
		if (escolhaGrupo >= 0 && escolhaGrupo < 0.4)
			grupoTorneio = getGrupoParticipantes(1, populacao.getIndividuos());
		
		else if (escolhaGrupo >= 0.4 && escolhaGrupo < 0.6)
			grupoTorneio = getGrupoParticipantes(2, populacao.getIndividuos());
			
		else if (escolhaGrupo >= 0.6 && escolhaGrupo < 0.8)
			grupoTorneio = getGrupoParticipantes(3, populacao.getIndividuos());
			
		else if (escolhaGrupo >= 0.8 && escolhaGrupo < 0.9)
			grupoTorneio = getGrupoParticipantes(4, populacao.getIndividuos());
			
		else if (escolhaGrupo >= 0.9 && escolhaGrupo < 1.0)
			grupoTorneio = getGrupoParticipantes(5, populacao.getIndividuos());
		
		return grupoTorneio;
	}
	
	/**
	 * Divis�o abstrata da primeira metade da popula��o passada nos par�metros em cinco grupos de quantidade igual de indiv�duos. 
	 * O m�todo retorna o grupo de indiv�duos correspondente ao inteiro passado (chamada com numeroGrupo == 1 retorna os primeiros 10% de indiv�duos)
	 */
	private List<ArvoreExpressao> getGrupoParticipantes(int numeroGrupo, List<ArvoreExpressao> individuosPopulacao)
	{
		int tamanhoGrupo = individuosPopulacao.size()/10; //quinta parte da metade da popula��o
		List<ArvoreExpressao> grupoSelecionado = new ArrayList<ArvoreExpressao>();
		
		for(int i = numeroGrupo * tamanhoGrupo; i < (numeroGrupo * tamanhoGrupo) + tamanhoGrupo; i++)
		{
			grupoSelecionado.add(individuosPopulacao.get(i - 1));
		}

		return grupoSelecionado;
	}
}
