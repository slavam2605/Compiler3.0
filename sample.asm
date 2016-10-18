global f
f:
    push rbp
    mov rbp, rsp
    sub rsp, 8
    mov [rbp - 8], rdi
    sub rsp, 8
    lea rax, [rbp - 16]
    push rax
    mov rax, [rbp - 8]
    push rax
    mov rax, [rbp - 8]
    pop r10
    add rax, r10
    pop r10
    mov [r10], rax
    lea rax, [rbp - 8]
    push rax
    mov rax, 12
    pop r10
    mov [r10], rax
    mov rax, [rbp - 8]
    add rsp, 16
    pop rbp
    ret
    add rsp, 16
    pop rbp
    ret

