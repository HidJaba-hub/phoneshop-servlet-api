$(document).ready(function () {
    const productsJsonInput = document.getElementById("products");
    const table = document.getElementById("productsTable").tBodies[0]
    let productsId = JSON.parse(productsJsonInput.value).map(({id}) => id);
    const rowId = new Map(productsId.map((id, i) => [id, table.rows[i]]));


    $('#productsTable thead tr td a').on('click', function(event) {
        event.preventDefault();
        var xmlHttpRequest = new XMLHttpRequest();
        xmlHttpRequest.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                productsId = JSON.parse(this.responseText);

                productsId.forEach((id) => {
                    if (rowId.has(id)) {
                        table.insertAdjacentElement('beforeend', rowId.get(id));
                    }
                })
            }
        };
        xmlHttpRequest.open("GET", event.target.href, true);
        xmlHttpRequest.send();
    })
});
