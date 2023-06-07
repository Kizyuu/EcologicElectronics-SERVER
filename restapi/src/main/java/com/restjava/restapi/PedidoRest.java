package com.restjava.restapi;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restjava.restapi.database.RepositorioPedido;
import com.restjava.restapi.pedido.Pedido;

@RestController
@RequestMapping("/pedido")
public class PedidoRest {
    @Autowired
    private RepositorioPedido repositorio;

    @GetMapping
    public List<Pedido> listarPedidos() {
        try {
            /*
             * retorna todos os registros de pedidos armazenados no repositório fazendo uso
             * de JpaRepository, que acessa o banco de dados
             */
            return repositorio.findAll();
        } catch (Exception e) {
            /*
             * Aqui vamos tratar o erro de exceção e
             * retornar uma mensagem de erro personalizada, para depois reportar ao cliente
             */
            e.printStackTrace();
            return Collections.emptyList(); // Retorna uma lista vazia como fallback
        }
    }

    @PutMapping
    public void alterarPedido(@RequestBody Pedido pedido) {
        try {
            // Se pedido tiver id maior que zero, atualiza no bd
            if (pedido.getId() > 0)
                repositorio.save(pedido);
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();
        }
    }

    @DeleteMapping
    public void deletarPedido(@RequestBody Pedido pedido) {
        try {
            // delet pedido
            repositorio.delete(pedido);
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();
        }
    }

    @PostMapping // POST do pedido, recebe o json no corpo da requisição
    public ResponseEntity<String> criarPedido(@RequestBody Pedido pedido) {
        try {
            // Aqui recebemos o json do pedido e salvamos no banco de dados
            Pedido novoPedido = repositorio.save(pedido);
            /*
             * quando criado e upado para o banco é retornado o status de criado e mensagem
             * de sucesso
             */
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Pedido criado com sucesso. ID: " + novoPedido.getId());
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();
            // Quando da erro ao criar pedido é enviado status de erro ao criar pedido
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar pedido");
        }
    }

    @GetMapping("/usuario/{userId}") // GET de todos os pedidos do usuario especificado
    public ResponseEntity<List<Pedido>> getPedidosByUserId(@PathVariable Long userId) {
        try {
            /*
             * Realiza uma busca por pedidos com base em um ID de usuário em um repositório
             * e retorna uma resposta HTTP contendo os pedidos encontrados.
             */

            List<Pedido> pedidos = repositorio.findByUserId(userId);
            return new ResponseEntity<>(pedidos, HttpStatus.OK);
        } catch (Exception e) {
            // Lidar com a exceção de alguma forma
            e.printStackTrace();
            // Quando da erro ao criar pedido é enviado status de erro ao criar pedido
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
