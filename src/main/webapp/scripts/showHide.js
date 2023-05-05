function showHide(index) {
    const el = document.getElementById("popup" + index);
    if (el.style.display === "none") {
        el.style.display = "block";
    } else {
        el.style.display = "none";
    }
}