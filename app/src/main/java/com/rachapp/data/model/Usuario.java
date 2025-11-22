package com.rachapp.data.model;

public class Usuario {
    private Long idUsuario;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private int avatarId;

    public Usuario() {}

    public Usuario(String nome, String email, String senha, String telefone, int avatarId) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.avatarId = avatarId;
    }

    public Long getIdUsuario() { return idUsuario; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public int getAvatarId() { return avatarId; }
    public void setAvatarId(int avatarId) { this.avatarId = avatarId; }
}