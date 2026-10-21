package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class EmpresaResourceTest {
    @Test
    void deveCadastrarEmpresaEConsultarPorId() {
        Integer id = given()
                .contentType("application/json")
                .body("""
                        {
                          "cnpj": "11222333000181",
                          "razaoSocial": "Acme Ltda",
                          "situacao": "ATIVA",
                          "endereco": {
                            "logradouro": "Rua Um",
                            "cidade": "São Paulo",
                            "estado": "SP",
                            "cep": "01000-000"
                          }
                        }
                        """)
                .when().post("/api/empresas")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("cnpj", equalTo("11222333000181"))
                .extract().path("id");

        given()
                .when().get("/api/empresas/{id}", id)
                .then()
                .statusCode(200)
                .body("razaoSocial", equalTo("Acme Ltda"))
                .body("endereco.estado", equalTo("SP"));
    }

    @Test
    void deveRejeitarCnpjInvalido() {
        given()
                .contentType("application/json")
                .body("""
                        {
                          "cnpj": "00000000000000",
                          "razaoSocial": "Empresa Inválida",
                          "situacao": "ATIVA"
                        }
                        """)
                .when().post("/api/empresas")
                .then()
                .statusCode(400)
                .body("message", equalTo("CNPJ inválido"));
    }
}
