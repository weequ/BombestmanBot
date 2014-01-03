echo "starting run.sh"
ant
echo "ant ran"
java -cp build/classes/ bombestmanbot.BombestmanBot $1 $2
echo "finished"
