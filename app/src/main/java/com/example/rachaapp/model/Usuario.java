package com.example.rachaapp.model;

public class Usuario {
    private Long idUsuario;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private int avatarId;

    // 1. Empty Constructor (REQUIRED for LoginDialog)
    public Usuario() {}

    // 2. Full Constructor (Used by CadastroActivity)
    public Usuario(String nome, String email, String senha, String telefone, int avatarId) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.avatarId = avatarId;
    }

    // --- Getters and Setters ---

    public Long getIdUsuario() { return idUsuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; } // Needed for Login

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; } // Needed for Login

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public int getAvatarId() { return avatarId; }
    public void setAvatarId(int avatarId) { this.avatarId = avatarId; }
}