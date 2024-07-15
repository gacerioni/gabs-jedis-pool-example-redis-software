package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Main {

    private static JedisPool pool;

    public static void main(String[] args) {
        String redisHost = "redis-16019.c308.sa-east-1-1.ec2.redns.redis-cloud.com"; // Substitua pelo host do seu Redis
        int redisPort = 16019; // Substitua pela porta do seu Redis
        String redisPassword = "blablabla"; // Substitua pela senha do seu Redis, se aplicável
        boolean useSsl = false; // Altere para true se estiver usando SSL

        // Configuração do pool
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10); // Número máximo de conexões
        poolConfig.setMaxIdle(5);   // Número máximo de conexões ociosas
        poolConfig.setMinIdle(1);   // Número mínimo de conexões ociosas
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        // Inicialização do pool
        if (redisPassword != null && !redisPassword.isEmpty()) {
            pool = new JedisPool(poolConfig, redisHost, redisPort, 20000, redisPassword, useSsl);
        } else {
            pool = new JedisPool(poolConfig, redisHost, redisPort, 20000, useSsl);
        }

        // Tentativa de conexão e execução de um comando simples
        try (Jedis jedis = pool.getResource()) {
            jedis.auth(redisPassword);
            System.out.println("Conexão estabelecida com sucesso!");

            // Exemplo de comando simples
            jedis.set("exemplo-chave", "exemplo-valor");
            String valor = jedis.get("exemplo-chave");
            System.out.println("Valor para 'exemplo-chave': " + valor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fechamento do pool
            if (pool != null) {
                pool.close();
            }
        }
    }
}