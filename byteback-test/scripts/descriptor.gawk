func descriptor_to_jvm(vc) {
    gsub(/'6/, ";", vc);
    gsub(/_/, ".", vc);
    gsub(/_/, ".", vc);
    gsub(/[Ii]'2/, "", vc);
    vc=gensub(/'5(.*)'5/, "<\\1>", "g", vc);
    vc=gensub(/'7(.*)'7/, "(\\1)", "g", vc);
    gsub(/'3/, "_", vc);
    gsub(/'4/, "$", vc);
    gsub(/'9/, "'", vc);
    gsub(/^Default'8./, "", vc);
    return vc
}