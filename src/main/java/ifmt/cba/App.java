package ifmt.cba;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.namespace.QName;
import ifmt.cba.servico.ProdutoVO;
import ifmt.cba.servico.ServicoControleEstoque;

import jakarta.xml.ws.Service;

public class App {
    private static ServicoControleEstoque controleEstoque;

    public static void main(String[] args) {

        URL url;
        try {
            url = new URL("http://localhost:8083/servico/estoque?wsdl");
            QName qname = new QName("http://servico.cba.ifmt/", "ServicoControleEstoqueImplService");
            Service service = Service.create(url, qname);
            controleEstoque = service.getPort(ServicoControleEstoque.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (controleEstoque != null) {
            String opcoesMenu = "[1] Adicionar produto\n[2] Remover Produto\n[3] "
                    + "Adicionar Estoque\n[4] Baixar Estoque\n"
                    + "[5] Contar Produtos\n[6] Contar Estoque Físico\n[7]"
                    + "Listar Produtos\n[8] totalizar valor do estoque do produto\n"
                    + "[9] totalizar valor de estoque geral\n [10] sair";
            int opcao;
            do {
                opcao = Integer.parseInt(JOptionPane.showInputDialog(null, opcoesMenu));
                switch (opcao) {
                    case 1:
                        novoProduto();
                        break;
                    case 2:
                        removerProduto();
                        break;
                    case 3:
                        adicionarEstoque();
                        break;
                    case 4:
                        baixarEstoque();
                        break;
                    case 5:
                        contarProdutos();
                        break;
                    case 6:
                        contarEstoqueProdutos();
                        break;
                    case 7:
                        listarProdutos();
                        break;

                    case 8:
                        totalizarValorEstoqueProduto();
                        break;

                    case 9:
                        totalizarValorEstoqueGeral();
                        break;

                }
            } while (opcao != 10);
        }

    }

    private static void novoProduto() {
        ProdutoVO produtoVOTemp = null;
        int codigo;
        String nome;
        float valorUnitario = 0;
        boolean Sair = false;
        do {
            try {
                codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "forneça o codigo do produto"));
                nome = JOptionPane.showInputDialog(null, "forneça o nome do produto");
                valorUnitario = Float.parseFloat(JOptionPane.showInputDialog(null, "forneça o valor do produto"));
                produtoVOTemp = new ProdutoVO();
                produtoVOTemp.setCodigo(codigo);
                produtoVOTemp.setNome(nome);
                produtoVOTemp.setValorUnitario(valorUnitario);
                controleEstoque.adicionarProduto(produtoVOTemp);
                Sair = true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao executar a operaçãp" + ex.getMessage());
            }
        } while (!Sair);
    }

    private static void removerProduto() {
        ProdutoVO produtoVOTemp = null;
        int codigo;
        try {
            codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneça o codigo do produto"));
            produtoVOTemp = controleEstoque.buscarProdutoPorCodigo(codigo);
            if (produtoVOTemp != null) {
                controleEstoque.removerProduto(produtoVOTemp);
            } else {
                JOptionPane.showMessageDialog(null, "Produto não localizado");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Dados inconsistentes");
        }

    }

    private static void adicionarEstoque() {
        ProdutoVO produtoVOTemp = null;
        int codigo;
        int quantidade;
        boolean sair = false;
        do {
            try {
                codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneça o codigo do produto"));
                produtoVOTemp = controleEstoque.buscarProdutoPorCodigo(codigo);
                if (produtoVOTemp != null) {
                    quantidade = Integer.parseInt(
                            JOptionPane.showInputDialog(null, "forneça a quantidade a ser adicionada ao estoque"));
                    controleEstoque.adicionarEstoqueProduto(produtoVOTemp, quantidade);
                    sair = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Produto não localizado");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "erro ao executar a operação" + ex.getMessage());
            }

        } while (!sair);
    }

    private static void baixarEstoque() {
        ProdutoVO produtoVOTemp = null;
        int codigo;
        int quantidade;
        boolean sair = false;
        do {
            try {
                codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneça o codigo do produto"));
                produtoVOTemp = controleEstoque.buscarProdutoPorCodigo(codigo);
                if (produtoVOTemp != null) {
                    quantidade = Integer.parseInt(
                            JOptionPane.showInputDialog(null, "forneça a quantidade a ser baixada do estoque"));
                    controleEstoque.baixarEstoqueProduto(produtoVOTemp, quantidade);
                    sair = true;
                } else {
                    JOptionPane.showMessageDialog(null, "produto não localizado");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "erro ao executar operação" + ex.getMessage());
            }
        } while (!sair);
    }

    private static void contarProdutos() {
        System.out.println("----------------------------");
        System.out.println("quantidade de produtos: " + controleEstoque.contadorProduto());
    }

    private static void contarEstoqueProdutos() {
        System.out.println("----------------------------");
        System.out.println("total estoque fisico dos produtos: " + controleEstoque.totalEstoqueFisico());
    }

    private static void listarProdutos() {
        List<ProdutoVO> listaProduto = controleEstoque.listaProduto();
        for (ProdutoVO produtoTemp : listaProduto) {
            System.out.println("----------------------------");
            System.out.println("codigo: " + produtoTemp.getCodigo());
            System.out.println("nome: " + produtoTemp.getNome());
            System.out.println("estoque: " + produtoTemp.getEstoque());
            System.out.println("valor: "+ produtoTemp.getValorUnitario());
        }
    }

    private static float totalizarValorEstoqueProduto() {
        ProdutoVO produtoVOTemp = null;
        int codigo;
        int quantidade;
        float valorUnitario;
        float total = 0;
        try {
            codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneça o código do produto"));
            produtoVOTemp = controleEstoque.buscarProdutoPorCodigo(codigo);
            if (produtoVOTemp != null) {
                quantidade = produtoVOTemp.getEstoque();
                valorUnitario = produtoVOTemp.getValorUnitario();
                // quantidade = Integer.parseInt(
                //         JOptionPane.showInputDialog(null, "Forneça a quantidade do produto"));
                // valorUnitario = Float.parseFloat(
                //         JOptionPane.showInputDialog(null, "Forneça o valor unitário do produto"));
                total = controleEstoque.totalizarValorEstoqueProduto(produtoVOTemp, quantidade, valorUnitario);
                System.out.println("total do valor do estoque do produto: "+ total);
            } else {
                JOptionPane.showMessageDialog(null, "Produto não localizado");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao calcular o valor do estoque do produto: " + ex.getMessage());
        }
        return total;
    }
    
    public static void totalizarValorEstoqueGeral() {
        try {
            float total = controleEstoque.totalizarValorEstoqueGeral();
            System.out.println("----------------------------");
            System.out.println("Total do estoque geral: " + total);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao calcular o valor do estoque geral: " + ex.getMessage());
        }
    }
}
