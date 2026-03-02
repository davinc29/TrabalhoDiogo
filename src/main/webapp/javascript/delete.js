// Função que mostra popup para confirmar decisão do usuário de deletar dados
function confirmarDelete(event) {
    // Bloqueia envio do formulário
    event.preventDefault();

    // Função da biblioteca "SweetAlert" para estilização do popup
    Swal.fire({
        // Caracterização básica
        title: "Tem certeza que deseja excluir?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#CC2a41",
        cancelButtonColor: "#fff",
        confirmButtonText: "Excluir",
        cancelButtonText: "Cancelar",

        // Caracterização isolada dos botões para quando o popup for aberto
        didOpen: (popup) => {
            const confirmButton = popup.querySelector('.swal2-confirm');
            const cancelButton = popup.querySelector('.swal2-cancel');

            popup.style.fontFamily = "Arial";
            popup.style.color = "black";
            confirmButton.style.color = "white";
            cancelButton.style.color = "black";
            confirmButton.style.fontWeight = "bold";
            cancelButton.style.fontWeight = "bold";
        }
    }).then((result) => {
        if (result.isConfirmed) {
            // Envia o formulário com os dados presentes dentro do 'form'
            event.target.submit();
        }
    });
}