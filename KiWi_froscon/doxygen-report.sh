echo "For creating doxygen reports install doxygen and graphviz"
rm -rf reports/doxy/
doxygen config/doxygen/Doxyfile
