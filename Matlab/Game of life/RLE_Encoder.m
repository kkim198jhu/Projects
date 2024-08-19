function RLE = RLE_Encoder(M)
    %Gets the general variables
    [x_length,y_length] = size(M);
    
    my_number = 1;
    count = 0;
    my_str = "";

    %Sets up the actual decoder
    for i = 1:x_length
        
        for j = 1:y_length
            %Gets the intial value
            if(j == 1)
                if(M(i,j) == 1)
                    stored_object = 'o';
                    my_number = 1;
                else
                    stored_object = 'b';
                    my_number = 0;
                end
                count = 1;
            else
                %Checks for numbers and how many are in a row
                if(M(i,j) == my_number)
                    count = count + 1;
                end
                if(M(i,j) ~= my_number || j == y_length)
                    if(i == x_length && j == y_length)
                         %Sees if last value is dead cell
                        if(stored_object == 'o')
                            if(count > 1)
                                    my_str = append(my_str, string(count), stored_object);
                                else
                                    my_str = append(my_str, stored_object);
                            end        
                        end
                    else
                        
                        %This sees where to place the number and letters
                        if(count > 1)
                            my_str = append(my_str, string(count), stored_object);
                        else
                            my_str = append(my_str, stored_object);
                        end
                        %Gets an exception
                        if(M(i,j) ~= my_number && j == y_length)
                            if(stored_object == 'o')
                                stored_object = 'b';
                            else
                                stored_object = 'o';
                            end
                             my_str = append(my_str, stored_object);
                        end
                        %Resets values
                        count = 1;
                        if(stored_object == 'o')
                            stored_object = 'b';
                            my_number = 0;
                        else
                            stored_object = 'o';
                            my_number = 1;
                        end
                       
                    end
                end

                
            end
        end
        %Sets up The ending of the lies
        if(i == x_length)
            my_str = append(my_str, "!");
        else
            my_str = append(my_str, "$");
        end
    end

    %Returns a cell matrix
    
    cell_1st = append('x = ', string(x_length), ', y = ', string(y_length), ', rule = B3/S23');
   
    RLE = {cell_1st ; my_str};
end