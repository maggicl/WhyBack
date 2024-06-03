BEGIN {
    print "file","kind","goal","result","time","steps"
}
/^File/ {
    file=$2 
}
/^Goal/ {
    vc=$2;
    gsub(/'vc\.$/, "", vc);
    orig=vc;

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
    goal=vc 
}
/^Prover/ {
    gsub(/[^0-9\\.]/, "", $5);
    gsub(/.$/, "", $5);
    gsub(/[^0-9\\.]/, "", $6);

    cmd="./decltype.sh \"" file "\" \"" orig "\""
    cmd | getline type
    close(cmd)

    print file,type,goal,$4,$5,$6
}
