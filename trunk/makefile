objects = WordsBattle.o \
		wb_controller.o \
        wb_dictionary.o \
        wb_view_console.o
CC = g++        
        
WordsBattle : $(objects) clean
	$(CC) -o WordsBattle $(objects)
        
WordsBattle.o : WordsBattle.cpp
	$(CC) -c WordsBattle.cpp
				       
wb_controller.o : wb_controller.cpp
	$(CC) -c wb_controller.cpp
             
wb_dictionary.o : wb_dictionary.cpp
	$(CC) -c wb_dictionary.cpp
             
wb_view_console.o : wb_view_console.cpp
	$(CC) -c wb_view_console.cpp
             
clean :
	rm WordsBattle $(objects)