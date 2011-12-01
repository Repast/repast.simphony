.First <- function() {

# try and find all the data sets we can
fileNumber <- 0;
while ("" != 
		eval.parent(
			parse(
				text=sprintf("Sys.getenv('LOG_FILE%s')" , fileNumber)
			)
		)
	) {

	fileName <- eval.parent(
					parse(
						text=sprintf("Sys.getenv('LOG_FILE%s')" , fileNumber)
					)
				);
				
	varName <- eval.parent(
					parse(
						text=sprintf("gsub('[^[:alnum:]]', '', Sys.getenv('LOG_FILE%s'))" , fileNumber)
					)
				);
				
	delimiter <- eval.parent(
					parse(
						text=sprintf("Sys.getenv('DELIMITER%s')" , fileNumber)
					)
				);

	try(
		eval.parent(
			parse(
				text=sprintf("%s=read.table('%s', header=TRUE, sep='%s')" , varName, fileName, delimiter)
			)
		)	
	)
#	cat(eval.parent(
#			parse(
#				text=sprintf("Sys.getenv('LOG_FILE%s')" , fileNumber)
#			)
#		));

	fileNumber <- fileNumber + 1;
}

#if(!is.null(Sys.getenv( 'LOG_FILE')))
#try(
#	eval.parent(
#		parse(
#			text=sprintf("Repast_data_set=read.table('%s', header=TRUE, sep=',')" , Sys.getenv('LOG_FILE'))
#		)
#	)
#)
#cat(sprintf("Repast_data_set=read.table('%s', header=TRUE, sep=',')" , Sys.getenv('LOG_FILE')))

#if(!is.null(Sys.getenv( 'LOG_FILES')))
#try(
#	eval.parent(
#		parse(
#			text=sprintf("Repast_data_set=read.table('%s', header=TRUE, sep=',')" , Sys.getenv('LOG_FILE'))
#		)
#	)
#)

#   cat(Sys.getenv('LOG_FILE'))

# doesn't do what i hoped it'd do options(Rcmdr=list(attach.data.set=TRUE))

library(grDevices)
library(Rcmdr)
#cat(.activeDataSet)

}