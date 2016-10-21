extern malloc
extern free
extern println_int
extern print_int
extern println
extern print_pair
global merge_sort
merge_sort:
    push rbp
    mov rbp, rsp
    sub rsp, 8
    mov [rbp - 8], rdi
    sub rsp, 8
    mov [rbp - 16], rsi
    sub rsp, 8
    mov [rbp - 24], rdx
    sub rsp, 8
    mov [rbp - 32], rcx
    mov r11, [rbp - 32]
    mov rdi, [rbp - 24]
    sub r11, rdi
    mov rdi, 1
    cmp r11, rdi
    je L0
    jmp L1
L0:
    mov rax, 0
    add rsp, 32
    pop rbp
    ret
L1:
    sub rsp, 8
    mov r11, [rbp - 24]
    mov rdi, [rbp - 32]
    add r11, rdi
    mov rdi, 2
    mov rax, r11
    xor rdx, rdx
    div rdi
    mov [rbp - 40], rax
    mov r11, [rbp - 32]
    mov rdi, [rbp - 24]
    sub r11, rdi
    mov rdi, 2
    cmp r11, rdi
    jg L2
    jmp L3
L2:
    mov rcx, [rbp - 40]
    mov r11, [rbp - 24]
    mov rdx, r11
    mov rsi, [rbp - 16]
    mov rdi, [rbp - 8]
    call merge_sort
    mov rcx, [rbp - 32]
    mov r11, [rbp - 40]
    mov rdx, r11
    mov rsi, [rbp - 16]
    mov rdi, [rbp - 8]
    call merge_sort
L3:
    sub rsp, 8
    mov r11, [rbp - 24]
    mov [rbp - 48], r11
    mov rax, r11
    sub rsp, 8
    mov r11, [rbp - 40]
    mov [rbp - 56], r11
    mov rax, r11
    sub rsp, 8
    mov r11, [rbp - 24]
    mov [rbp - 64], r11
    mov rax, r11
L4:
    mov r11, [rbp - 48]
    mov rdi, [rbp - 40]
    cmp r11, rdi
    jl L7
    jmp L5
L7:
    mov rdi, [rbp - 56]
    mov rsi, [rbp - 32]
    cmp rdi, rsi
    jl L6
    jmp L5
L6:
    sub rsp, 8
    mov r11, [rbp - 8]
    mov rdi, 8
    mov rsi, [rbp - 48]
    mov rax, rdi
    mul rsi
    add r11, rax
    mov r11, [r11]
    mov [rbp - 72], r11
    mov rax, r11
    sub rsp, 8
    mov r11, [rbp - 8]
    mov rdi, 8
    mov rsi, [rbp - 56]
    mov rax, rdi
    mul rsi
    add r11, rax
    mov r11, [r11]
    mov [rbp - 80], r11
    mov rax, r11
    mov r11, [rbp - 72]
    mov rdi, [rbp - 80]
    cmp r11, rdi
    jl L8
    jmp L9
L8:
    mov r11, [rbp - 16]
    mov rdi, 8
    mov rsi, [rbp - 64]
    mov rax, rdi
    mul rsi
    add r11, rax
    mov rdi, [rbp - 72]
    mov [r11], rdi
    mov rax, rdi
    mov r11, [rbp - 48]
    mov rdi, 1
    add r11, rdi
    mov [rbp - 48], r11
    mov rax, r11
    mov r11, [rbp - 64]
    mov rdi, 1
    add r11, rdi
    mov [rbp - 64], r11
    mov rax, r11
L9:
    mov r11, [rbp - 80]
    mov rdi, [rbp - 72]
    cmp r11, rdi
    jle L10
    jmp L11
L10:
    mov r11, [rbp - 16]
    mov rdi, 8
    mov rsi, [rbp - 64]
    mov rax, rdi
    mul rsi
    add r11, rax
    mov rdi, [rbp - 80]
    mov [r11], rdi
    mov rax, rdi
    mov r11, [rbp - 56]
    mov rdi, 1
    add r11, rdi
    mov [rbp - 56], r11
    mov rax, r11
    mov r11, [rbp - 64]
    mov rdi, 1
    add r11, rdi
    mov [rbp - 64], r11
    mov rax, r11
L11:
    add rsp, 16
    mov rax, 1
    jmp L4
L5:
L12:
    mov r11, [rbp - 48]
    mov rdi, [rbp - 40]
    cmp r11, rdi
    jl L14
    jmp L13
L14:
    mov r11, [rbp - 16]
    mov rdi, 8
    mov rsi, [rbp - 64]
    mov rax, rdi
    mul rsi
    add r11, rax
    mov rdi, [rbp - 8]
    mov rsi, 8
    mov rcx, [rbp - 48]
    mov rax, rsi
    mul rcx
    add rdi, rax
    mov rdi, [rdi]
    mov [r11], rdi
    mov rax, rdi
    mov r11, [rbp - 64]
    mov rdi, 1
    add r11, rdi
    mov [rbp - 64], r11
    mov rax, r11
    mov r11, [rbp - 48]
    mov rdi, 1
    add r11, rdi
    mov [rbp - 48], r11
    mov rax, r11
    jmp L12
L13:
L15:
    mov r11, [rbp - 56]
    mov rdi, [rbp - 32]
    cmp r11, rdi
    jl L17
    jmp L16
L17:
    mov r11, [rbp - 16]
    mov rdi, 8
    mov rsi, [rbp - 64]
    mov rax, rdi
    mul rsi
    add r11, rax
    mov rdi, [rbp - 8]
    mov rsi, 8
    mov rcx, [rbp - 56]
    mov rax, rsi
    mul rcx
    add rdi, rax
    mov rdi, [rdi]
    mov [r11], rdi
    mov rax, rdi
    mov r11, [rbp - 64]
    mov rdi, 1
    add r11, rdi
    mov [rbp - 64], r11
    mov rax, r11
    mov r11, [rbp - 56]
    mov rdi, 1
    add r11, rdi
    mov [rbp - 56], r11
    mov rax, r11
    jmp L15
L16:
    sub rsp, 8
    mov r11, [rbp - 24]
    mov [rbp - 72], r11
    mov rax, r11
L18:
    mov r11, [rbp - 72]
    mov rdi, [rbp - 32]
    cmp r11, rdi
    jl L20
    jmp L19
L20:
    mov r11, [rbp - 8]
    mov rdi, 8
    mov rsi, [rbp - 72]
    mov rax, rdi
    mul rsi
    add r11, rax
    mov rdi, [rbp - 16]
    mov rsi, 8
    mov rcx, [rbp - 72]
    mov rax, rsi
    mul rcx
    add rdi, rax
    mov rdi, [rdi]
    mov [r11], rdi
    mov rax, rdi
    mov r11, [rbp - 72]
    mov rdi, 1
    add r11, rdi
    mov [rbp - 72], r11
    mov rax, r11
    jmp L18
L19:
    add rsp, 8
    mov rax, 0
    add rsp, 64
    pop rbp
    ret
    add rsp, 64
    pop rbp
    ret
global f
f:
    push rbp
    mov rbp, rsp
    sub rsp, 8
    mov [rbp - 8], rdi
    sub rsp, 8
    mov [rbp - 16], rsi
    sub rsp, 8
    mov r11, 8
    mov rdi, [rbp - 16]
    mov rax, r11
    mul rdi
    mov rdi, rax
    call malloc
    mov [rbp - 24], rax
    mov rcx, [rbp - 16]
    mov r11, 0
    mov rdx, r11
    mov rsi, [rbp - 24]
    mov rdi, [rbp - 8]
    call merge_sort
    mov rdi, [rbp - 24]
    call free
    add rsp, 24
    pop rbp
    ret

