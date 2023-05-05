function showHid(index) {
    const el = document.getElementById(index);
    if (el.style.display === "none") {
        el.style.display = "block";
    } else {
        el.style.display = "none";
    }
}