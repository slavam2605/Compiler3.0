global f
f:
    push rbp
    mov rbp, rsp
    mov rax, 1000000000000
    push rax
    mov rax, 2000000000000
    pop rcx
    add rax, rcx
    pop rbp
    ret
    pop rbp
    ret

