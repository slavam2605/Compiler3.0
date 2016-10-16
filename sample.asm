global f
f:
    push rbp
    mov rbp, rsp
    sub rsp, 8
    mov [rbp - 8], rdi
    mov rax, [rbp - 8]
    push rax
    mov rax, 1
    pop r10
    cmp r10, rax
    jl L1
L0:
    xor rax, rax
    jmp L2
L1:
    mov rax, 1
L2:
    test rax, rax
    jz L3
    mov rax, 1
    add rsp, 8
    pop rbp
    ret
L3:
    mov rax, [rbp - 8]
    push rax
    mov rax, [rbp - 8]
    push rax
    mov rax, 1
    pop r10
    mov r11, rax
    mov rax, r10
    sub rax, r11
    push rax
    pop rdi
    call f
    pop r10
    mul r10
    add rsp, 8
    pop rbp
    ret
    add rsp, 8
    pop rbp
    ret

