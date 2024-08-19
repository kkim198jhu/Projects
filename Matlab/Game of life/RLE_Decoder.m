
function next_gen_mat = RLE_Decoder(txt_filename)
%This function takes in txt files that have been converted from rle and
%makes a matric out of them. Then it returns a matric using the rules of
%life game simulator, after a certain amount of iterations.

%Imports the data that is now in a txt file
text = importdata(txt_filename);

%Gets the dimensions of the matrix
splitDigits = regexp(text(1), "\d*", 'match');
x_size = str2double(splitDigits{1}{1});
y_size = str2double(splitDigits{1}{2});
new_matrix = zeros(y_size, x_size);

%Gets the seperated columns
str = text(2);
expression = "\$";
splitStr = regexp(str, expression, 'split');

%Rules each line
for i = 1:y_size
    loc = regexp(splitStr{1}{i},"[ob]");
    true_count = 1;
    for j = 1:length(loc)
        %Counts how many times it should repete
        counter = 1;
        if(j == 1)
            if(loc(j) ~= 1)
                counter = str2double(splitStr{1}{i}(1:loc(j)-1));
            end
        else
            if(loc(j-1)+1 ~= loc(j))
                counter = str2double(splitStr{1}{i}(loc(j-1)+1:loc(j)-1));
            end
        end
        %Adds either 0's or 1's a certain amount of times depending on
        %count
        for k = 1:counter
            if(splitStr{1}{i}(loc(j)) == 'o')
                new_matrix(i,true_count) = 1;
            else
                new_matrix(i,true_count) = 0;
            end
            true_count = true_count + 1;
        end
    end
end

next_gen_mat = new_matrix;
end
